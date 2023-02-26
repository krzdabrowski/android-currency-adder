package eu.krzdabrowski.currencyadder.basefeature.domain.model

data class UserSaving(
    val id: Int = 0,
    val place: String,
    val saving: Double,
    val currency: String
)
