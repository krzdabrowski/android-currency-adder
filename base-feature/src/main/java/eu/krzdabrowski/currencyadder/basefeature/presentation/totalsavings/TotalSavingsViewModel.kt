package eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetAllCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetTotalUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.UpdateChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.UpdateChosenCurrencyCodeForTotalSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.ChosenCurrencyCodeChanged
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.CurrencyCodesFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.Error
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.TotalUserSavingsFetched
import eu.krzdabrowski.currencyadder.core.presentation.mvi.BaseViewModel
import eu.krzdabrowski.currencyadder.core.utils.toFormattedAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

@HiltViewModel
class TotalSavingsViewModel @Inject constructor(
    private val getTotalUserSavingsUseCase: GetTotalUserSavingsUseCase,
    private val getAllCurrencyCodesUseCase: GetAllCurrencyCodesUseCase,
    private val getChosenCurrencyCodeForTotalSavingsUseCase: GetChosenCurrencyCodeForTotalSavingsUseCase,
    private val updateChosenCurrencyCodeForTotalSavingsUseCase: UpdateChosenCurrencyCodeForTotalSavingsUseCase,
    savedStateHandle: SavedStateHandle,
    totalSavingsInitialState: TotalSavingsUiState,
) : BaseViewModel<TotalSavingsUiState, PartialState, Nothing, TotalSavingsIntent>(
        savedStateHandle = savedStateHandle,
        initialState = totalSavingsInitialState,
    ) {

    init {
        getAllCurrencyCodes()
        observeTotalUserSavings()
        observeChosenCurrencyCodeForTotalSavings()
    }

    override fun mapIntents(intent: TotalSavingsIntent): Flow<PartialState> = when (intent) {
        is UpdateChosenCurrencyCodeForTotalSavings -> updateChosenCurrencyCodeForTotalSavings(intent.currencyCode)
    }

    override fun reduceUiState(
        previousState: TotalSavingsUiState,
        partialState: PartialState,
    ): TotalSavingsUiState = when (partialState) {
        is TotalUserSavingsFetched -> previousState.copy(
            totalUserSavings = partialState.totalUserSavings,
        )

        is CurrencyCodesFetched -> previousState.copy(
            currencyCodes = partialState.currencyCodes,
        )

        is ChosenCurrencyCodeChanged -> previousState.copy(
            chosenCurrencyCode = partialState.currencyCode,
        )

        is Error -> previousState
    }

    private fun getAllCurrencyCodes() = acceptChanges(
        getAllCurrencyCodesUseCase()
            .map {
                it.fold(
                    onSuccess = { currencyCodes ->
                        if (currencyCodes.isNotEmpty()) {
                            CurrencyCodesFetched(currencyCodes)
                        } else {
                            Error
                        }
                    },
                    onFailure = {
                        Error
                    },
                )
            },
    )

    private fun observeTotalUserSavings() = acceptChanges(
        getTotalUserSavingsUseCase()
            .map {
                it.fold(
                    onSuccess = { total ->
                        TotalUserSavingsFetched(total.toFormattedAmount())
                    },
                    onFailure = {
                        Error
                    },
                )
            },
    )

    private fun observeChosenCurrencyCodeForTotalSavings() = acceptChanges(
        getChosenCurrencyCodeForTotalSavingsUseCase()
            .map {
                it.fold(
                    onSuccess = { chosenCode ->
                        if (chosenCode.isNotEmpty()) {
                            ChosenCurrencyCodeChanged(chosenCode)
                        } else {
                            updateChosenCurrencyCodeForTotalSavingsUseCase(
                                BASE_EXCHANGE_RATE_CODE,
                            )
                            ChosenCurrencyCodeChanged(BASE_EXCHANGE_RATE_CODE)
                        }
                    },
                    onFailure = {
                        Error
                    },
                )
            },
    )

    private fun updateChosenCurrencyCodeForTotalSavings(code: String): Flow<PartialState> = flow {
        updateChosenCurrencyCodeForTotalSavingsUseCase(code)
    }
}
