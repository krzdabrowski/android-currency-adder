package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.UpdateUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.GetCurrencyCodes
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.GetUserSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UpdateUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.CurrencyCodesFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Loading
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.UserSavingsFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

private val emptyUserSaving = UserSavingDisplayable(
    place = "",
    saving = "0",
    currency = BASE_EXCHANGE_RATE_CODE
)

@HiltViewModel
class CurrencyAdderViewModel @Inject constructor(
    private val getUserSavingsUseCase: GetUserSavingsUseCase,
    private val addUserSavingUseCase: AddUserSavingUseCase,
    private val updateUserSavingUseCase: UpdateUserSavingUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    private val getCurrencyCodesUseCase: GetCurrencyCodesUseCase,
    savedStateHandle: SavedStateHandle,
    currencyAdderInitialState: CurrencyAdderUiState
) : BaseViewModel<CurrencyAdderUiState, PartialState, CurrencyAdderEvent, CurrencyAdderIntent>(
    savedStateHandle,
    currencyAdderInitialState
) {
    init {
        acceptIntent(GetCurrencyCodes)
        acceptIntent(GetUserSavings)
        acceptIntent(RefreshExchangeRates)
    }

    override fun mapIntents(intent: CurrencyAdderIntent): Flow<PartialState> = when (intent) {
        is GetUserSavings -> getUserSavings()
        is AddUserSaving -> addUserSaving()
        is UpdateUserSaving -> updateUserSaving(intent.updatedSaving)

        is RefreshExchangeRates -> refreshExchangeRates()
        is GetCurrencyCodes -> getCurrencyCodes()
    }

    override fun reduceUiState(
        previousState: CurrencyAdderUiState,
        partialState: PartialState
    ): CurrencyAdderUiState = when (partialState) {
        is Loading -> previousState.copy(
            isLoading = true,
            isError = false
        )
        is UserSavingsFetched -> previousState.copy(
            isLoading = false,
            userSavings = partialState.userSavings,
            isError = false
        )
        is CurrencyCodesFetched -> previousState.copy(
            currencyCodes = partialState.currencyCodes
        )
        is Error -> previousState.copy(
            isLoading = false,
            isError = true
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
            emptyUserSaving.toDomainModel()
        )
            .onFailure {
                emit(Error(it))
            }
    }

    private fun updateUserSaving(updatedUserSaving: UserSavingDisplayable): Flow<PartialState> = flow {
        updateUserSavingUseCase(
            updatedUserSaving.toDomainModel()
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
