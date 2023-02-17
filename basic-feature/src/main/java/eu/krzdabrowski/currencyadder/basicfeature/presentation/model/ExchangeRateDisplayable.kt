package eu.krzdabrowski.currencyadder.basicfeature.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeRateDisplayable(
    val currencyCode: String,
    val currencyRate: String
) : Parcelable
