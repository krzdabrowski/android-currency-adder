package eu.krzdabrowski.currencyadder.basicfeature.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import eu.krzdabrowski.currencyadder.basicfeature.R
import eu.krzdabrowski.currencyadder.basicfeature.presentation.model.ExchangeRateDisplayable

const val ROCKET_DIVIDER_TEST_TAG = "rocketDividerTestTag"

@Composable
fun RocketsListContent(
    rocketList: List<ExchangeRateDisplayable>,
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
            items = rocketList,
            key = { _, rocket -> rocket.currencyCode }
        ) { index, item ->
            RocketItem(
                rocket = item,
                onRocketClick = { onRocketClick("") }
            )

            if (index < rocketList.lastIndex) {
                Divider(
                    modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG)
                )
            }
        }
    }
}
