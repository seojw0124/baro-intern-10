package com.jeongu.barointernapp.presentation.component.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jeongu.barointernapp.R

@Composable
fun LikeIcon(
    isLiked: Boolean,
    onClick: () -> Unit
) {
    val iconRes = if (isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outlined

    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = stringResource(R.string.description_like_icon),
        modifier = Modifier
            .size(20.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        tint = Color.Unspecified
    )
}