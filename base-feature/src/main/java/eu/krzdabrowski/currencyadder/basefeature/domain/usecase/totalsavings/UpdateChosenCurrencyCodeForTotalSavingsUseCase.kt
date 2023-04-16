package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.totalsavings

fun interface UpdateChosenCurrencyCodeForTotalSavingsUseCase : suspend (String) -> Result<Unit>
