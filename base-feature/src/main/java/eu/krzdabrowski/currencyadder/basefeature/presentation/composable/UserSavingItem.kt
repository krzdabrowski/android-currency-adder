package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection.EndToStart
import androidx.compose.material3.DismissDirection.StartToEnd
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue.Default
import androidx.compose.material3.DismissValue.DismissedToEnd
import androidx.compose.material3.DismissValue.DismissedToStart
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.extensions.DebounceEffect

private const val SWIPE_ICON_SIZE_INACTIVE_PERCENTAGE = 0.75f
private const val MAX_FRACTIONAL_DIGITS = 2
private const val FRACTIONAL_LIMITER: Char = '.'
private const val FRACTIONAL_DEFAULT_VALUE: Char = 'x'

@Composable
fun UserSavingItem(
    item: UserSavingDisplayable,
    currencyCodes: List<String>,
    onItemUpdate: (UserSavingDisplayable) -> Unit,
    onItemRemove: (UserSavingDisplayable) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentItem by rememberUpdatedState(item)
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it != Default) {
                onItemRemove(currentItem)
                true
            } else {
                false
            }
        },
    )

    SwipeToDismiss(
        state = dismissState,
        background = { SwipeBackground(dismissState) },
        dismissContent = {
            UserSavingItemContent(
                item = item,
                currencyCodes = currencyCodes,
                onItemUpdate = onItemUpdate,
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return

    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissedToStart,
            DismissedToEnd,
            -> Color.Red
            Default -> Color.LightGray
        },
    )

    val alignment = when (direction) {
        StartToEnd -> Alignment.CenterStart
        EndToStart -> Alignment.CenterEnd
    }

    val scale by animateFloatAsState(
        if (dismissState.targetValue == Default) SWIPE_ICON_SIZE_INACTIVE_PERCENTAGE else 1f,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Remove user saving",
            modifier = Modifier
                .align(alignment)
                .scale(scale),
        )
    }
}

@Composable
private fun UserSavingItemContent(
    item: UserSavingDisplayable,
    currencyCodes: List<String>,
    modifier: Modifier = Modifier,
    onItemUpdate: (UserSavingDisplayable) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag(
                stringResource(R.string.user_saving_content_description),
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserSavingPlace(
            item = item,
            modifier = Modifier.weight(1f),
            onItemUpdate = onItemUpdate,
        )

        VerticalDivider()

        UserSavingAmount(
            item = item,
            modifier = Modifier.weight(1f),
            onItemUpdate = onItemUpdate,
        )

        VerticalDivider()

        UserSavingCurrencyDropdownMenu(
            value = item.currency,
            currencyCodes = currencyCodes,
            modifier = Modifier.weight(1f),
            onCurrencyChange = {
                onItemUpdate(
                    item.copy(currency = it),
                )
            },
        )
    }
}

@Composable
private fun UserSavingPlace(
    item: UserSavingDisplayable,
    modifier: Modifier = Modifier,
    onItemUpdate: (UserSavingDisplayable) -> Unit,
) {
    var input by remember {
        mutableStateOf(item.place)
    }

    TextField(
        value = input,
        onValueChange = { input = it },
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
        ),
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )

    DebounceEffect(
        input = input,
        operation = {
            if (it != item.place) {
                onItemUpdate(
                    item.copy(place = input),
                )
            }
        },
    )
}

@Composable
private fun UserSavingAmount(
    item: UserSavingDisplayable,
    modifier: Modifier = Modifier,
    onItemUpdate: (UserSavingDisplayable) -> Unit,
) {
    var currentInput by remember {
        mutableStateOf(item.amount)
    }

    TextField(
        value = currentInput,
        onValueChange = { newInput ->
            currentInput = when {
                newInput.isEmpty() -> newInput
                !newInput.isValidAmount() -> currentInput
                !newInput.isValidFractional() -> currentInput
                else -> newInput
            }
        },
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
        ),
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )

    DebounceEffect(
        input = currentInput,
        operation = {
            if (it != item.amount) {
                onItemUpdate(
                    item.copy(amount = currentInput),
                )
            }
        },
    )
}

@Composable
private fun UserSavingCurrencyDropdownMenu(
    value: String,
    currencyCodes: List<String>,
    modifier: Modifier = Modifier,
    onCurrencyChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        TextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            ),
            shape = RectangleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            currencyCodes.forEach { code ->
                DropdownMenuItem(
                    text = { Text(code) },
                    onClick = {
                        onCurrencyChange(code)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier,
) {
    Divider(
        modifier = modifier
            .fillMaxHeight()
            .width(1.dp),
    )
}

private fun String.isValidAmount() =
    toDoubleOrNull() != null

private fun String.isValidFractional() =
    getOrElse(length - MAX_FRACTIONAL_DIGITS - 2) { FRACTIONAL_DEFAULT_VALUE } != FRACTIONAL_LIMITER
