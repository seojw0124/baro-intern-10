package com.jeongu.barointernapp.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jeongu.barointernapp.R
import com.jeongu.barointernapp.SampleProduct
import com.jeongu.barointernapp.component.HomeToolbar
import com.jeongu.barointernapp.component.ScrollToTopButton
import com.jeongu.barointernapp.presentation.model.HomeUiState
import com.jeongu.barointernapp.presentation.model.ProductModel
import com.jeongu.barointernapp.presentation.viewmodel.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val likedMap by viewModel.likedMap.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    // 화면 진입 시 상품 목록을 다시 로드 (좋아요 상태 등이 변경됐을 수 있음)
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeToolbar()

            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is HomeUiState.Success -> {
                    val products = (uiState as HomeUiState.Success).products
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(products) { product ->
                            ProductItem(
                                product = product,
                                isLiked = likedMap[product.id] ?: false,
                                onLikeClick = { viewModel.toggleLike(product.id) },
                                onClick = {
                                    navController.navigate("product_detail/${product.id}")
                                }
                            )
                        }
                    }
                }

                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = (uiState as HomeUiState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )

                            Button(onClick = { viewModel.loadProducts() }) {
                                Text("다시 시도")
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showScrollToTop,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 20.dp)
        ) {
            ScrollToTopButton {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: ProductModel,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 130.dp)
        ) {
            // 이미지 로딩 (URL 또는 리소스 구분)
            AsyncImage(
                model = product.imageUrl,
                contentDescription = stringResource(id = R.string.description_product_thumbnail),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            // 오른쪽 콘텐츠 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 상단 콘텐츠 영역
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = product.tradingPlace,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${product.price}원",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // 하단 아이콘 영역
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_comment),
                        contentDescription = stringResource(id = R.string.description_comment_icon),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${product.commentCount}")

                    Spacer(modifier = Modifier.width(8.dp))

                    LikeIcon(
                        isLiked = isLiked,
                        onClick = onLikeClick
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${product.likeCount}")
                }
            }
        }
    }
}

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