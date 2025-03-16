package com.jeongu.barointernapp.presentation.component.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeongu.barointernapp.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BottomActionBar(
    price: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // 변경: spacedBy에서 SpaceBetween으로
        ) {
            // 좋아요 버튼
            IconButton(onClick = onLikeClick) {
                Icon(
                    painter = painterResource(
                        id = if (isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outlined
                    ),
                    contentDescription = stringResource(R.string.description_like_icon),
                    tint = if (isLiked) Color.Red else Color.Unspecified
                )
            }

            // 가격 정보 - 중앙 정렬을 위해 Box로 감싸기
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.format_prodcut_price,
                        NumberFormat.getNumberInstance(Locale.KOREA).format(price)
                    ),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // 채팅하기 버튼
            Button(
                onClick = { /* 채팅 기능 */ },
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("채팅하기")
            }
        }
    }
}