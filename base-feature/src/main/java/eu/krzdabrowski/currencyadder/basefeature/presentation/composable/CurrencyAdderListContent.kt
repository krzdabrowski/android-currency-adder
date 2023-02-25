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
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable

const val USER_SAVING_DIVIDER_TEST_TAG = "userSavingDividerTestTag"

@Composable
fun CurrencyAdderListContent(
    userSavingList: List<UserSavingDisplayable>,
    modifier: Modifier = Modifier,
    onUserSavingClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_medium)
            )
    ) {
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
