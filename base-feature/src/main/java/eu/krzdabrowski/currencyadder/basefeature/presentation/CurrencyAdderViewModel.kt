package eu.krzdabrowski.currencyadder.basefeature.presentation

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.RefreshExchangeRatesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.AddUserSavingUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.usersavings.GetUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.AddUserSaving
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.GetUserSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.RefreshExchangeRates
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UpdateUserSavingAmount
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.ChooseUserSavingCurrency
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderIntent.UpdateUserSavingPlace
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Fetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.CurrencyAdderUiState.PartialState.Loading
import eu.krzdabrowski.currencyadder.basefeature.presentation.mapper.toDomainModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.mapper.toPresentationModel
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

@HiltViewModel
class CurrencyAdderViewModel @Inject constructor(
    private val getUserSavingsUseCase: GetUserSavingsUseCase,
    private val addUserSavingUseCase: AddUserSavingUseCase,
    private val refreshExchangeRatesUseCase: RefreshExchangeRatesUseCase,
    savedStateHandle: SavedStateHandle,
    currencyAdderInitialState: CurrencyAdderUiState
) : BaseViewModel<CurrencyAdderUiState, PartialState, CurrencyAdderEvent, CurrencyAdderIntent>(
    savedStateHandle,
    currencyAdderInitialState
) {
    init {
        acceptIntent(RefreshExchangeRates)
        acceptIntent(GetUserSavings)
    }

    override fun mapIntents(intent: CurrencyAdderIntent): Flow<PartialState> = when (intent) {
        is GetUserSavings -> getUserSavings()
        is AddUserSaving -> addUserSaving()
        is RefreshExchangeRates -> refreshExchangeRates()
        is ChooseUserSavingCurrency -> userSavingCurrencyClicked(intent.userSavingId)
        is UpdateUserSavingAmount -> emptyFlow()
        is UpdateUserSavingPlace -> emptyFlow()
    }

    override fun reduceUiState(
        previousState: CurrencyAdderUiState,
        partialState: PartialState
    ): CurrencyAdderUiState = when (partialState) {
        is Loading -> previousState.copy(
            isLoading = true,
            isError = false
        )
        is Fetched -> previousState.copy(
            isLoading = false,
            userSavings = partialState.list,
            isError = false
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
                        emit(Fetched(userSavingList.map { it.toPresentationModel() }))
                    }
                    .onFailure {
                        emit(Error(it))
                    }
            }
    }

    private fun addUserSaving(): Flow<PartialState> = flow {
        addUserSavingUseCase(
            createEmptyUserSaving().toDomainModel()
        )
            .onFailure {
                emit(Error(it))
            }
    }

    private fun createEmptyUserSaving() = UserSavingDisplayable(
        place = "",
        saving = "0",
        currency = BASE_EXCHANGE_RATE_CODE
    )

    private fun refreshExchangeRates(): Flow<PartialState> = flow {
        refreshExchangeRatesUseCase()
            .onFailure {
                emit(Error(it))
            }
    }

    private fun userSavingCurrencyClicked(userSavingId: Int): Flow<PartialState> {
        return emptyFlow()
    }
}
