package eu.krzdabrowski.currencyadder.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.delay

private const val INPUT_DEBOUNCE_VALUE_IN_MILLIS = 400L

@Composable
fun DebounceEffect(
    input: String,
    debounceValue: Long = INPUT_DEBOUNCE_VALUE_IN_MILLIS,
    operation: (String) -> Unit,
) {
    val latestOperation by rememberUpdatedState(operation)

    LaunchedEffect(input) {
        delay(debounceValue)
        latestOperation(input)
    }
}
