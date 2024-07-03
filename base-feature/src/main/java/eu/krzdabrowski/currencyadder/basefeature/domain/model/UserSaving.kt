package eu.krzdabrowski.currencyadder.basefeature.domain.model

import java.util.UUID

data class UserSaving(
    val id: Long? = null,
    val uuid: UUID,
    val place: String,
    val amount: Double,
    val currency: String,
)
