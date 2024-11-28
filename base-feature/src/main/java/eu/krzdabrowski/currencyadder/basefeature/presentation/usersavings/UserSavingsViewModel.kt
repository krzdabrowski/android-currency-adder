package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesThatStartWithUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingPositionsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.GetCurrencyCodesThatStartWith
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RemoveUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSavingPositions
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.CurrencyCodesFiltered
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.CurrencyCodesFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.Loading
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.UserSavingsFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.presentation.mvi.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

private const val ROCKETS_REFRESH_FAILURE_INDICATOR_DURATION_IN_MILLIS = 500L // to make refresh indicator visible for a while

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

private val emptyUserSavingTemplate = UserSavingDisplayable(
    position = -1,
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
    private val updateUserSavingPositionsUseCase: UpdateUserSavingPositionsUseCase,
    private val removeUserSavingUseCase: RemoveUserSavingUseCase,
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
        observeCurrencyCodes()
        observeUserSavings()
    }

    override fun mapIntents(intent: UserSavingsIntent): Flow<PartialState> = when (intent) {
        is AddUserSaving -> addUserSaving()
        is UpdateUserSaving -> updateUserSaving(intent.updatedSaving)
        is RemoveUserSaving -> removeUserSaving(intent.removedUserSavingId)
        is UpdateUserSavingPositions -> updateUserSavingPositions(
            intent.movedItemId,
            intent.fromListItemIndex,
            intent.toListItemIndex,
        )
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

        is UserSavingsFetched -> {
            previousState.copy(
                isLoading = false,
                userSavings = partialState.userSavings.map {
                    it.copy(
                        currencyPossibilities = previousState.currencyCodes,
                    )
                },
                isError = false,
            )
        }

        is CurrencyCodesFetched -> {
            previousState.copy(
                isLoading = false,
                currencyCodes = partialState.currencyCodes,
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
                userSavings = (updatedSavings + restSavings).sortedBy { it.position },
            )
        }

        is Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    private fun observeCurrencyCodes() = acceptChanges(
        getAllCurrencyCodesUseCase().map { result ->
            result.fold(
                onSuccess = {
                    CurrencyCodesFetched(it)
                },
                onFailure = {
                    Error(IllegalStateException("Something went wrong during retrieval of currency codes"))
                },
            )
        }.onStart { emit(Loading) },
    )

    private fun observeUserSavings() = acceptChanges(
        getUserSavingsUseCase().map { result ->
            result.fold(
                onSuccess = { list ->
                    UserSavingsFetched(
                        userSavings = list.map { it.toPresentationModel() },
                    )
                },
                onFailure = {
                    Error(IllegalStateException("Something went wrong during retrieval of user savings"))
                },
            )
        }.onStart { emit(Loading) },
    )

    private fun addUserSaving(): Flow<PartialState> = flow {
        val emptyUserSaving = emptyUserSavingTemplate.copy(
            position = uiState.value.userSavings.size,
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

    private fun updateUserSavingPositions(
        movedItemId: Long,
        fromListItemIndex: Int,
        toListItemIndex: Int,
    ): Flow<PartialState> = flow {
        updateUserSavingPositionsUseCase(
            movedItemId,
            fromListItemIndex,
            toListItemIndex,
        ).onFailure {
            emit(Error(it))
        }
    }

    private fun refreshExchangeRates(): Flow<PartialState> = flow<PartialState> {
        refreshExchangeRatesUseCase()
            .onFailure {
                delay(ROCKETS_REFRESH_FAILURE_INDICATOR_DURATION_IN_MILLIS)
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
