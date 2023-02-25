package eu.krzdabrowski.currencyadder.basefeature.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSavingDisplayable(
    val id: Int,
    val location: String,
    val saving: String,
    val currency: String
) : Parcelable