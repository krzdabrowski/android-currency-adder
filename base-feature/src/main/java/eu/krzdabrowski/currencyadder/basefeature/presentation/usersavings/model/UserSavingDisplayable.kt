package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSavingDisplayable(
    val id: Int = 0,
    val place: String,
    val amount: String,
    val currency: String,
) : Parcelable
