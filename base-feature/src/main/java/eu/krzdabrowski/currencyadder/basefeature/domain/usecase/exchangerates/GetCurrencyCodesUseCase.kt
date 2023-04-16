package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

import kotlinx.coroutines.flow.Flow

fun interface GetCurrencyCodesUseCase : () -> Flow<Result<List<String>>>
