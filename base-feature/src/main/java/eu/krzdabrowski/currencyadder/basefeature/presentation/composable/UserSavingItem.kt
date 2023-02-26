package eu.krzdabrowski.currencyadder.basefeature.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import eu.krzdabrowski.currencyadder.basefeature.R
import eu.krzdabrowski.currencyadder.basefeature.presentation.model.UserSavingDisplayable
import eu.krzdabrowski.currencyadder.core.ui.Typography

@Composable
fun UserSavingItem(
    userSaving: UserSavingDisplayable,
    modifier: Modifier = Modifier,
    onUserSavingClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                vertical = dimensionResource(id = R.dimen.dimen_medium)
            )
            .testTag(
                stringResource(R.string.user_saving_content_description)
            )
            .clickable { onUserSavingClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = userSaving.place,
            style = Typography.titleMedium
        )

        Text(
            text = userSaving.saving,
            style = Typography.bodyMedium
        )

        Text(
            text = userSaving.currency,
            style = Typography.bodyMedium
        )
    }
}
