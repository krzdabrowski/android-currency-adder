package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.ExchangeRateDisplayable

const val ROCKET_DIVIDER_TEST_TAG = "rocketDividerTestTag"

@Composable
fun CurrencyAdderListContent(
    exchangeRateList: List<ExchangeRateDisplayable>,
    modifier: Modifier = Modifier,
    onRocketClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_medium)
            )
    ) {
        itemsIndexed(
            items = exchangeRateList,
            key = { _, rocket -> rocket.currencyCode }
        ) { index, item ->
            UserSavingItem(
                exchangeRate = item,
                onRocketClick = { onRocketClick("") }
            )

            if (index < exchangeRateList.lastIndex) {
                Divider(
                    modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG)
                )
            }
        }
    }
}
