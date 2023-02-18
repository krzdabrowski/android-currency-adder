package eu.krzdabrowski.currencyadder.basefeature.data.remote.api

import eu.krzdabrowski.currencyadder.basefeature.data.remote.model.ExchangeRatesResponse
import retrofit2.http.GET

interface ExchangeRatesApi {

    @GET("exchangerates/tables/a")
    suspend fun getExchangeRates(): List<ExchangeRatesResponse>
}
