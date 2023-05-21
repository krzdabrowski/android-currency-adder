package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

fun interface GetCurrencyCodesThatStartWithUseCase : suspend (String) -> Result<List<String>>
