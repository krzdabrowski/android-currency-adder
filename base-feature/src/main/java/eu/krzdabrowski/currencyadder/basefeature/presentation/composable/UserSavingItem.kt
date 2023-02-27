package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

private const val DEFAULT_SAVING_VALUE = "0.0"

@Composable
fun UserSavingItem(
    item: UserSavingDisplayable,
    currencyCodes: List<String>,
    modifier: Modifier = Modifier,
    onItemUpdate: (UserSavingDisplayable) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag(
                stringResource(R.string.user_saving_content_description)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserSavingPlace(
            item = item,
            modifier = Modifier.weight(1f),
            onItemUpdate = onItemUpdate
        )

        VerticalDivider()

        UserSavingAmount(
            item = item,
            modifier = Modifier.weight(1f),
            onItemUpdate = onItemUpdate
        )

        VerticalDivider()

        UserSavingCurrencyDropdownMenu(
            value = item.currency,
            currencyCodes = currencyCodes,
            modifier = Modifier.weight(1f),
            onCurrencyChange = {
                onItemUpdate(
                    item.copy(currency = it)
                )
            }
        )
    }
}

@Composable
private fun UserSavingPlace(
    item: UserSavingDisplayable,
    modifier: Modifier = Modifier,
    onItemUpdate: (UserSavingDisplayable) -> Unit
) {
    TextField(
        value = TextFieldValue(
            text = item.place,
            selection = TextRange(item.place.length)
        ),
        onValueChange = {
            onItemUpdate(
                item.copy(place = it.text)
            )
        },
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun UserSavingAmount(
    item: UserSavingDisplayable,
    modifier: Modifier = Modifier,
    onItemUpdate: (UserSavingDisplayable) -> Unit
) {
    TextField(
        value = TextFieldValue(
            text = if (item.saving != DEFAULT_SAVING_VALUE) item.saving else "",
            selection = TextRange(item.place.length)
        ),
        onValueChange = {
            onItemUpdate(
                item.copy(saving = it.text)
            )
        },
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun UserSavingCurrencyDropdownMenu(
    value: String,
    currencyCodes: List<String>,
    modifier: Modifier = Modifier,
    onCurrencyChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencyCodes.forEach { code ->
                DropdownMenuItem(
                    text = { Text(code) },
                    onClick = {
                        onCurrencyChange(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}
