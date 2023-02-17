package eu.krzdabrowski.currencyadder.basicfeature.data.remote.api

import eu.krzdabrowski.currencyadder.basicfeature.data.remote.model.ExchangeRatesResponse
import retrofit2.http.GET

interface ExchangeRatesApi {

    @GET("exchangerates/tables/a")
    suspend fun getExchangeRates(): List<ExchangeRatesResponse>
}
