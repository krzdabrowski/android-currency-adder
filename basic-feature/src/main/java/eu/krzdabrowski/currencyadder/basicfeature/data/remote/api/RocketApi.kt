package eu.krzdabrowski.currencyadder.basicfeature.data.remote.api

import eu.krzdabrowski.currencyadder.basicfeature.data.remote.model.RocketResponse
import retrofit2.http.GET

interface RocketApi {

    @GET("rockets")
    suspend fun getRockets(): List<RocketResponse>
}
