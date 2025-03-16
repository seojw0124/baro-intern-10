package com.jeongu.barointernapp.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeongu.barointernapp.R

@Composable
fun HomeToolbar(
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.label_home_toolbar_title),
                style = MaterialTheme.typography.titleLarge
            )
//            Spacer(modifier = Modifier.width(4.dp))
//            Icon(
//                painter = painterResource(id = R.drawable.ic_arrow_down),
//                contentDescription = null,
//                modifier = Modifier.size(16.dp)
//            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = stringResource(id = R.string.description_notification_icon),
            modifier = Modifier
                .size(24.dp)
                .clickable { onNotificationClick() }
        )
    }
}