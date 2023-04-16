package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

fun interface RefreshExchangeRatesUseCase : suspend () -> Result<Unit>
