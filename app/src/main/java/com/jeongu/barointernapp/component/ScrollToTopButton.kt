package com.jeongu.barointernapp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeongu.barointernapp.R
import com.jeongu.barointernapp.ui.theme.Carrot
import com.jeongu.barointernapp.ui.theme.Gray100

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = if (isPressed) Carrot else Gray100
    val iconRes = if (isPressed) R.drawable.ic_arrow_up_white else R.drawable.ic_arrow_up_gray

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = stringResource(id = R.string.description_scroll_to_top_icon),
        modifier = Modifier
            .size(40.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(10.dp)
    )
}