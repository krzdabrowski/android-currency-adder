package eu.krzdabrowski.currencyadder.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.delay

private const val INPUT_DEBOUNCE_VALUE_IN_MILLIS = 1000L

@Composable
fun DebounceEffect(
    input: TextFieldValue,
    debounceValue: Long = INPUT_DEBOUNCE_VALUE_IN_MILLIS,
    operation: () -> Unit
) {
    LaunchedEffect(input) {
        delay(debounceValue)
        operation()
    }
}
