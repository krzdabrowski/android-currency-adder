package eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates.GetCurrencyCodesUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.GetTotalUserSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings.UpdateChosenCurrencyCodeForTotalSavingsUseCase
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.GetChosenCurrencyCodeForTotalSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.GetCurrencyCodes
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.GetTotalUserSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsIntent.UpdateChosenCurrencyCodeForTotalSavings
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.ChosenCurrencyCodeChanged
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.CurrencyCodesFetched
import eu.krzdabrowski.currencyadder.basefeature.presentation.totalsavings.TotalSavingsUiState.PartialState.TotalUserSavingsFetched
import eu.krzdabrowski.currencyadder.core.base.BaseViewModel
import eu.krzdabrowski.currencyadder.core.extensions.toFormattedAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val BASE_EXCHANGE_RATE_CODE = "PLN"

@HiltViewModel
class TotalSavingsViewModel @Inject constructor(
    private val getTotalUserSavingsUseCase: GetTotalUserSavingsUseCase,
    private val getCurrencyCodesUseCase: GetCurrencyCodesUseCase,
    private val getChosenCurrencyCodeForTotalSavingsUseCase: GetChosenCurrencyCodeForTotalSavingsUseCase,
    private val updateChosenCurrencyCodeForTotalSavingsUseCase: UpdateChosenCurrencyCodeForTotalSavingsUseCase,
    savedStateHandle: SavedStateHandle,
    totalSavingsInitialState: TotalSavingsUiState,
) : BaseViewModel<TotalSavingsUiState, PartialState, Nothing, TotalSavingsIntent>(
    savedStateHandle,
    totalSavingsInitialState,
) {
    init {
        acceptIntent(GetCurrencyCodes)
        acceptIntent(GetTotalUserSavings)
        acceptIntent(GetChosenCurrencyCodeForTotalSavings)
    }

    override fun mapIntents(intent: TotalSavingsIntent): Flow<PartialState> = when (intent) {
        is GetTotalUserSavings -> getTotalUserSavings()
        is GetCurrencyCodes -> getCurrencyCodes()
        is GetChosenCurrencyCodeForTotalSavings -> getChosenCurrencyCodeForTotalSavings()
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
    }

    private fun getCurrencyCodes(): Flow<PartialState> = flow {
        getCurrencyCodesUseCase()
            .collect { result ->
                result
                    .onSuccess {
                        if (it.isNotEmpty()) {
                            emit(CurrencyCodesFetched(it))
                        }
                    }
            }
    }

    private fun getTotalUserSavings(): Flow<PartialState> = flow {
        getTotalUserSavingsUseCase()
            .collect { result ->
                result
                    .onSuccess {
                        emit(TotalUserSavingsFetched(it.toFormattedAmount()))
                    }
            }
    }

    private fun getChosenCurrencyCodeForTotalSavings(): Flow<PartialState> =
        flow {
            getChosenCurrencyCodeForTotalSavingsUseCase()
                .collect { result ->
                    result
                        .onSuccess {
                            if (it.isNotEmpty()) {
                                emit(ChosenCurrencyCodeChanged(it))
                            } else {
                                updateChosenCurrencyCodeForTotalSavingsUseCase(
                                    BASE_EXCHANGE_RATE_CODE,
                                )
                            }
                        }
                }
        }

    private fun updateChosenCurrencyCodeForTotalSavings(currencyCode: String): Flow<PartialState> =
        flow {
            updateChosenCurrencyCodeForTotalSavingsUseCase(currencyCode)
        }
}
