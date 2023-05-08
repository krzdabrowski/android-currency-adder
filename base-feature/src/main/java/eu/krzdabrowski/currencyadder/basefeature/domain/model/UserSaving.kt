package eu.krzdabrowski.currencyadder.basefeature.domain.model

data class UserSaving(
    val id: Long? = null,
    val place: String,
    val amount: Double,
    val currency: String,
)
