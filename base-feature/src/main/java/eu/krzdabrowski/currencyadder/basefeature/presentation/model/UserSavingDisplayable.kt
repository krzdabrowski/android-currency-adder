package eu.krzdabrowski.currencyadder.basefeature.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSavingDisplayable(
    val id: Int = 0,
    val place: String,
    val saving: String,
    val currency: String
) : Parcelable
