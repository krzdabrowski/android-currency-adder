package eu.krzdabrowski.currencyadder.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

private const val INPUT_DEBOUNCE_VALUE_IN_MILLIS = 1000L

@Composable
fun DebounceEffect(
    input: String,
    debounceValue: Long = INPUT_DEBOUNCE_VALUE_IN_MILLIS,
    operation: () -> Unit,
) {
    LaunchedEffect(input) {
        delay(debounceValue)
        operation()
    }
}
