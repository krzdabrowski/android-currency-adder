package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

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
            .padding(
                vertical = dimensionResource(id = R.dimen.dimen_medium)
            )
            .testTag(
                stringResource(R.string.user_saving_content_description)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = item.place,
            onValueChange = {
                onItemUpdate(
                    item.copy(place = it)
                )
            },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        TextField(
            value = item.saving,
            onValueChange = {
                onItemUpdate(
                    item.copy(saving = it)
                )
            },
            modifier = Modifier.weight(1f),
            singleLine = true
        )

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
            readOnly = true
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
