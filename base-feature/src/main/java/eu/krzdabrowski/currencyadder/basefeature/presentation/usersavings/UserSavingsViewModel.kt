package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.RemoveUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.GetCurrencyCodes
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.GetUserSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.RemoveUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.CurrencyCodesFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.Loading
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.UserSavingsUiState.PartialState.UserSavingsPartialState.UserSavingsFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

private val emptyUserSaving = UserSavingDisplayable(
    place = "",
    amount = "0",
    currency = BASE_EXCHANGE_RATE_CODE,
)

@HiltViewModel
class UserSavingsViewModel @Inject constructor(
    private val getUserSavingsUseCase: GetUserSavingsUseCase,
    private val addUserSavingUseCase: AddUserSavingUseCase,
    private val updateUserSavingUseCase: UpdateUserSavingUseCase,
    private val removeUserSavingUseCase: RemoveUserSavingUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val getCurrencyCodesUseCase: GetCurrencyCodesUseCase,
    savedStateHandle: SavedStateHandle,
    userSavingsInitialState: UserSavingsUiState,
) : BaseViewModel<UserSavingsUiState, PartialState, Nothing, UserSavingsIntent>(
    savedStateHandle,
    userSavingsInitialState,
) {
    init {
        acceptIntent(GetCurrencyCodes)
        acceptIntent(GetUserSavings)
        acceptIntent(RefreshExchangeRates)
    }

    override fun mapIntents(intent: UserSavingsIntent): Flow<PartialState> = when (intent) {
        is GetUserSavings -> getUserSavings()
        is AddUserSaving -> addUserSaving()
        is UpdateUserSaving -> updateUserSaving(intent.updatedSaving)
        is RemoveUserSaving -> removeUserSaving(intent.removedSaving)
        is RefreshExchangeRates -> refreshExchangeRates()
        is GetCurrencyCodes -> getCurrencyCodes()
    }

    override fun reduceUiState(
        previousState: UserSavingsUiState,
        partialState: PartialState,
    ): UserSavingsUiState = when (partialState) {
        is Loading -> previousState.copy(
            isLoading = true,
            isError = false,
        )

        is UserSavingsFetched -> previousState.copy(
            isLoading = false,
            userSavings = partialState.userSavings,
            isError = false,
        )

        is CurrencyCodesFetched -> previousState.copy(
            currencyCodes = partialState.currencyCodes,
        )

        is Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    private fun getUserSavings(): Flow<PartialState> = flow {
        getUserSavingsUseCase()
            .onStart {
                emit(Loading)
            }
            .collect { result ->
                result
                    .onSuccess { userSavingList ->
                        emit(UserSavingsFetched(userSavingList.map { it.toPresentationModel() }))
                    }
                    .onFailure {
                        emit(Error(it))
                    }
            }
    }

    private fun addUserSaving(): Flow<PartialState> = flow {
        addUserSavingUseCase(
            emptyUserSaving.toDomainModel(),
        )
            .onFailure {
                emit(Error(it))
            }
    }

    private fun updateUserSaving(updatedUserSaving: UserSavingDisplayable): Flow<PartialState> =
        flow {
            updateUserSavingUseCase(
                updatedUserSaving.toDomainModel(),
            )
                .onFailure {
                    emit(Error(it))
                }
        }

    private fun removeUserSaving(removedUserSaving: UserSavingDisplayable): Flow<PartialState> =
        flow {
            removeUserSavingUseCase(
                removedUserSaving.toDomainModel(),
            )
                .onFailure {
                    emit(Error(it))
                }
        }

    private fun refreshExchangeRates(): Flow<PartialState> = flow {
        refreshExchangeRatesUseCase()
            .onFailure {
                emit(Error(it))
            }
    }

    private fun getCurrencyCodes(): Flow<PartialState> = flow {
        getCurrencyCodesUseCase()
            .onSuccess {
                emit(CurrencyCodesFetched(it))
            }
            .onFailure {
                emit(Error(it))
            }
    }
}
