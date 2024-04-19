package com.rafver.read.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.read.R
import com.rafver.read.ui.models.ReadViewEvent
import com.rafver.core_ui.models.UserUiModel

@Composable
fun UserListItem(
    user: UserUiModel,
    onViewEvent: (ReadViewEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = { onViewEvent(ReadViewEvent.OnListItemClicked(user.id)) },
            )
    ) {
        Column(
            modifier = modifier.padding(Dimensions.NORMAL_100)
        ) {
            Text(text = user.name, style = MaterialTheme.typography.titleLarge)
            Text(text = "${stringResource(id = R.string.lbl_age)}: ${user.age}")
            Text(text = "${stringResource(id = R.string.lbl_email)}: ${user.email}")
        }
    }
}

@Preview
@Composable
private fun PreviewUserListItem() {
    SimpleCRUDTheme {
        UserListItem(
            user = UserUiModel(
                id = "1",
                name = "John",
                age = 30,
                email = "john@doe.com",
            ),
            onViewEvent = {},
        )
    }
}