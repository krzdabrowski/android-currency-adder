package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSavingDisplayable(
    val id: Long = 0L,
    val position: Int,
    val place: String,
    val amount: String,
    val currency: String,
    val currencyPossibilities: List<String>,
) : Parcelable
