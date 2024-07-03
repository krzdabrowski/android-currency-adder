package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesThatStartWithUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.SwapUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.GetCurrencyCodesThatStartWith
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RemoveUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.SwapUserSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.CurrencyCodesFiltered
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.Loading
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.UserSavingsWithCurrencyCodesFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.presentation.mvi.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import java.util.UUID
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

private val emptyUserSavingTemplate = UserSavingDisplayable(
    place = "",
    amount = "0",
    currency = BASE_EXCHANGE_RATE_CODE,
    currencyPossibilities = emptyList(),
)

@HiltViewModel
class UserSavingsViewModel @Inject constructor(
    private val getUserSavingsUseCase: GetUserSavingsUseCase,
    private val addUserSavingUseCase: AddUserSavingUseCase,
    private val updateUserSavingUseCase: UpdateUserSavingUseCase,
    private val removeUserSavingUseCase: RemoveUserSavingUseCase,
    private val swapUserSavingsUseCase: SwapUserSavingsUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val getAllCurrencyCodesUseCase: GetAllCurrencyCodesUseCase,
    private val getCurrencyCodesThatStartWithUseCase: GetCurrencyCodesThatStartWithUseCase,
    savedStateHandle: SavedStateHandle,
    userSavingsInitialState: UserSavingsUiState,
) : BaseViewModel<UserSavingsUiState, PartialState, Nothing, UserSavingsIntent>(
        savedStateHandle = savedStateHandle,
        initialState = userSavingsInitialState,
    ) {

    init {
        observeUserSavingsWithAllCurrencyCodes()
    }

    override fun mapIntents(intent: UserSavingsIntent): Flow<PartialState> = when (intent) {
        is AddUserSaving -> addUserSaving()
        is UpdateUserSaving -> updateUserSaving(intent.updatedSaving)
        is RemoveUserSaving -> removeUserSaving(intent.removedUserSavingId)
        is SwapUserSavings -> swapUserSavings(intent.fromListItemIndex, intent.toListItemIndex)
        is GetCurrencyCodesThatStartWith -> getCurrencyCodesThatStartWith(intent.searchPhrase, intent.userSavingId)
        is RefreshExchangeRates -> refreshExchangeRates()
    }

    override fun reduceUiState(
        previousState: UserSavingsUiState,
        partialState: PartialState,
    ): UserSavingsUiState = when (partialState) {
        is Loading -> previousState.copy(
            isLoading = true,
            isError = false,
        )

        is UserSavingsWithCurrencyCodesFetched -> {
            previousState.copy(
                isLoading = false,
                userSavings = partialState.userSavings.map {
                    it.copy(
                        currencyPossibilities = partialState.currencyCodes,
                    )
                },
                isError = false,
            )
        }

        is CurrencyCodesFiltered -> {
            val (matchedSavings, restSavings) = previousState.userSavings.partition { it.id == partialState.userSavingId }
            val updatedSavings = matchedSavings.map {
                it.copy(
                    currencyPossibilities = partialState.filteredCurrencyCodes,
                )
            }

            previousState.copy(
                userSavings = (updatedSavings + restSavings).sortedBy { it.id },
            )
        }

        is Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    private fun observeUserSavingsWithAllCurrencyCodes() = acceptChanges(
        getUserSavingsUseCase()
            .combine(
                getAllCurrencyCodesUseCase(),
            ) { userSavingsResult, currencyCodesResult ->
                if (userSavingsResult.isFailure || currencyCodesResult.isFailure) {
                    return@combine Error(
                        IllegalStateException("Something went wrong during retrieval of either user savings, or currency codes"),
                    )
                }

                val userSavingList = userSavingsResult.fold(
                    onSuccess = { userSavingList ->
                        userSavingList.map { it.toPresentationModel() }
                    },
                    onFailure = { emptyList() },
                )

                val currencyCodesList = currencyCodesResult.fold(
                    onSuccess = { it },
                    onFailure = { emptyList() },
                )

                return@combine UserSavingsWithCurrencyCodesFetched(
                    userSavings = userSavingList,
                    currencyCodes = currencyCodesList,
                )
            }
            .onStart { emit(Loading) },
    )

    private fun addUserSaving(): Flow<PartialState> = flow {
        val emptyUserSaving = emptyUserSavingTemplate.copy(
            uuid = UUID.randomUUID().toString(),
        )

        addUserSavingUseCase(
            emptyUserSaving.toDomainModel(),
        )
            .onFailure {
                emit(Error(it))
            }
    }

    private fun updateUserSaving(userSaving: UserSavingDisplayable): Flow<PartialState> = flow {
        updateUserSavingUseCase(
            userSaving.toDomainModel(),
        )
            .onFailure {
                emit(Error(it))
            }
    }

    private fun removeUserSaving(userSavingId: Long): Flow<PartialState> = flow {
        removeUserSavingUseCase(userSavingId)
            .onFailure {
                emit(Error(it))
            }
    }

    private fun swapUserSavings(
        fromListItemIndex: Int,
        toListItemIndex: Int,
    ): Flow<PartialState> = flow {
        // SQLite starts indexing from 1; the whole CS world starts indexing from 0
        val fromDatabaseIndex = fromListItemIndex + 1L
        val toDatabaseIndex = toListItemIndex + 1L

        swapUserSavingsUseCase(
            fromDatabaseIndex,
            toDatabaseIndex,
        ).onFailure {
            emit(Error(it))
        }
    }

    private fun refreshExchangeRates(): Flow<PartialState> = flow<PartialState> {
        refreshExchangeRatesUseCase()
            .onFailure {
                emit(Error(it))
            }
    }.onStart {
        emit(Loading)
    }

    private fun getCurrencyCodesThatStartWith(
        searchPhrase: String,
        userSavingId: Long,
    ): Flow<PartialState> = flow {
        getCurrencyCodesThatStartWithUseCase(searchPhrase)
            .onSuccess { currencyCodes ->
                emit(CurrencyCodesFiltered(currencyCodes, userSavingId))
            }
            .onFailure {
                emit(Error(it))
            }
    }
}
