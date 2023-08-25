package eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.composable

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
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.usersavings.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.utils.DebounceEffect

private const val SWIPE_ICON_SIZE_INACTIVE_PERCENTAGE = 0.75f
private const val MAX_TOTAL_DIGITS = 12
private const val MAX_FRACTIONAL_DIGITS = 2
private const val FRACTIONAL_LIMITER: Char = '.'
private const val FRACTIONAL_DEFAULT_VALUE: Char = 'x'

@Composable
fun UserSavingItem(
    item: UserSavingDisplayable,
    onItemUpdate: (UserSavingDisplayable) -> Unit,
    onItemRemove: (Long) -> Unit,
    onCurrencyCodesUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentItem by rememberUpdatedState(item)
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            currentItem.id?.let { userSavingId ->
                if (dismissValue != Default) {
                    onItemRemove(userSavingId)
                    true
                } else {
                    false
                }
            } ?: false
        },
    )

    SwipeToDismiss(
        state = dismissState,
        background = { SwipeBackground(dismissState) },
        dismissContent = {
            UserSavingItemContent(
                item = item,
                onItemUpdate = onItemUpdate,
                onCurrencyCodesUpdate = onCurrencyCodesUpdate,
            )
        },
        modifier = modifier
            .testTag(
                stringResource(R.string.user_saving_content_description),
            ),
    )
}

@Composable
private fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return

    val color by animateColorAsState(
        targetValue = when (dismissState.targetValue) {
            DismissedToStart,
            DismissedToEnd,
            -> Color.Red
            Default -> Color.LightGray
        },
        label = "Item color change",
    )

    val alignment = when (direction) {
        StartToEnd -> Alignment.CenterStart
        EndToStart -> Alignment.CenterEnd
    }

    val scale by animateFloatAsState(
        targetValue = if (dismissState.targetValue == Default) SWIPE_ICON_SIZE_INACTIVE_PERCENTAGE else 1f,
        label = "Item icon size",
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
    onItemUpdate: (UserSavingDisplayable) -> Unit,
    onCurrencyCodesUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UserSavingPlace(
            place = item.place,
            modifier = Modifier.weight(1f),
            onPlaceUpdate = {
                onItemUpdate(
                    item.copy(place = it),
                )
            },
        )

        VerticalDivider()

        UserSavingAmount(
            amount = item.amount,
            modifier = Modifier.weight(1f),
            onAmountUpdate = {
                onItemUpdate(
                    item.copy(amount = it),
                )
            },
        )

        VerticalDivider()

        UserSavingChosenCurrencyDropdownMenu(
            currency = item.currency,
            currencyCodes = item.currencyPossibilities,
            modifier = Modifier.weight(1f),
            onCurrencyUpdate = {
                onItemUpdate(
                    item.copy(currency = it),
                )
            },
            onCurrencyCodesUpdate = onCurrencyCodesUpdate,
        )
    }
}

@Composable
private fun UserSavingPlace(
    place: String,
    modifier: Modifier = Modifier,
    onPlaceUpdate: (String) -> Unit,
) {
    var currentInput by remember {
        mutableStateOf(place)
    }

    TextField(
        value = currentInput,
        onValueChange = { currentInput = it },
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
        ),
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )

    DebounceEffect(
        input = currentInput,
        operation = onPlaceUpdate,
    )
}

@Composable
private fun UserSavingAmount(
    amount: String,
    modifier: Modifier = Modifier,
    onAmountUpdate: (String) -> Unit,
) {
    var currentInput by remember {
        mutableStateOf(amount)
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
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )

    DebounceEffect(
        input = currentInput,
        operation = onAmountUpdate,
    )
}

@Composable
private fun UserSavingChosenCurrencyDropdownMenu(
    currency: String,
    currencyCodes: List<String>,
    onCurrencyUpdate: (String) -> Unit,
    onCurrencyCodesUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var currentInput by remember {
        mutableStateOf(currency)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        TextField(
            value = currentInput,
            onValueChange = {
                currentInput = it.uppercase()
                expanded = true
            },
            modifier = Modifier
                .menuAnchor()
                .onFocusChanged {
                    if (!it.isFocused && currentInput != currency) {
                        currentInput = currency
                    }
                },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
            ),
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
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
                        currentInput = code
                        onCurrencyUpdate(code)
                        expanded = false
                    },
                )
            }
        }
    }

    LaunchedEffect(currentInput) {
        onCurrencyCodesUpdate(currentInput)
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
    toDoubleOrNull() != null && length <= MAX_TOTAL_DIGITS

private fun String.isValidFractional() =
    getOrElse(length - MAX_FRACTIONAL_DIGITS - 2) { FRACTIONAL_DEFAULT_VALUE } != FRACTIONAL_LIMITER
