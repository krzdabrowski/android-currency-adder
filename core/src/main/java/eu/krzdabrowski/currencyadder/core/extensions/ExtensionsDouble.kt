package eu.krzdabrowski.currencyadder.core.extensions

import java.util.Locale

fun Double.toFormattedAmount() =
    String.format(Locale.ENGLISH, "%.2f", this)
