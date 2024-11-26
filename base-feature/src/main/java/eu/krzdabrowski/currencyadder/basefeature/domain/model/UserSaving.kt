package eu.krzdabrowski.currencyadder.basefeature.domain.model

data class UserSaving(
    val id: Long = 0L,
    val position: Int,
    val place: String,
    val amount: Double,
    val currency: String,
)
