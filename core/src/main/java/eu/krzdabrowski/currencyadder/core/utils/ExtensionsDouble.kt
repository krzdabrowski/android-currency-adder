package eu.krzdabrowski.currencyadder.core.utils

import java.util.Locale

fun Double.toFormattedAmount() = String.format(Locale.ENGLISH, "%.2f", this)
