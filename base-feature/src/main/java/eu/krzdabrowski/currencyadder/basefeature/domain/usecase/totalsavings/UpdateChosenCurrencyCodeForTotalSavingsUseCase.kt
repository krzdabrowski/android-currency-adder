package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings

import eu.krzdabrowski.currencyadder.basefeature.domain.repository.TotalSavingsRepository
import eu.krzdabrowski.currencyadder.core.extensions.resultOf

fun interface UpdateChosenCurrencyCodeForTotalSavingsUseCase : suspend (String) -> Result<Unit>

suspend fun updateChosenCurrencyCodeForTotalSavings(
    totalSavingsRepository: TotalSavingsRepository,
    currencyCode: String,
): Result<Unit> = resultOf {
    totalSavingsRepository.updateChosenCurrencyCodeForTotalSavings(currencyCode)
}
