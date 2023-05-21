package eu.krzdabrowski.currencyadder.basefeature.domain.usecase.exchangerates

import kotlinx.coroutines.flow.Flow

fun interface GetAllCurrencyCodesUseCase : () -> Flow<Result<List<String>>>
