package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

const val USER_SAVING_DIVIDER_TEST_TAG = "userSavingDividerTestTag"
private val headerStringResources = listOf(
    R.string.list_header_place,
    R.string.list_header_saving,
    R.string.list_header_currency
)

@Composable
fun CurrencyAdderListContent(
    userSavingList: List<UserSavingDisplayable>,
    modifier: Modifier = Modifier,
    onUserSavingClick: (Int) -> Unit
) {


    LazyColumn(
        modifier = modifier
    ) {
        header()

        itemsIndexed(
            items = userSavingList,
            key = { _, userSaving -> userSaving.id }
        ) { index, item ->
            UserSavingItem(
                userSaving = item,
                onUserSavingClick = { onUserSavingClick(item.id) }
            )

            if (index < userSavingList.lastIndex) {
                Divider(
                    modifier = Modifier.testTag(USER_SAVING_DIVIDER_TEST_TAG)
                )
            }
        }
    }
}

private fun LazyListScope.header() {
    stickyHeader {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    vertical = dimensionResource(R.dimen.dimen_small)
                ),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            for (headerId in headerStringResources) {
                Text(
                    text = stringResource(headerId),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
