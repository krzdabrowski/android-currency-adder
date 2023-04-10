package eu.krzdabrowski.currencyadder.basefeature.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRatesResponse(
    @SerialName("rates")
    val exchangeRates: List<ExchangeRate> = emptyList(),
) {
    @Serializable
    data class ExchangeRate(
        @SerialName("code")
        val code: String = "",

        @SerialName("mid")
        val rate: Double = 0.0,
    )
}
