package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSavingDisplayable(
    val id: Long? = null,
    val uuid: String = "",
    val place: String,
    val amount: String,
    val currency: String,
    val currencyPossibilities: List<String>,
) : Parcelable
