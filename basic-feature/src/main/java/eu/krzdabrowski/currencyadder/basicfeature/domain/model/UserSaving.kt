package eu.krzdabrowski.currencyadder.basicfeature.domain.model

data class UserSaving(
    val id: Int = 0,
    val location: String,
    val saving: Double,
    val currency: String
)
