package com.jeongu.barointernapp.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.jeongu.barointernapp.presentation.component.HomeToolbar
import com.jeongu.barointernapp.presentation.component.ScrollToTopButton
import com.jeongu.barointernapp.presentation.model.HomeUiState
import com.jeongu.barointernapp.presentation.model.ProductModel
import com.jeongu.barointernapp.presentation.viewmodel.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val likedMap by viewModel.likedMap.collectAsState()
    val likeCountMap by viewModel.likeCountMap.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Snackbar 상태
    val snackbarHostState = remember { SnackbarHostState() }

    // 삭제 대화상자 상태
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    // 화면 진입 시 상품 목록을 다시 로드
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    // 업데이트된 제품 정보가 있는지 확인
    val updatedProduct = navController.currentBackStackEntry?.savedStateHandle?.get<ProductModel>("updatedProduct")

    // 업데이트된 제품 정보가 있으면 ViewModel에 전달
    LaunchedEffect(updatedProduct) {
        updatedProduct?.let {
            viewModel.updateProductInList(it)
        }
    }

    // 삭제 대화상자
    if (showDeleteDialog && productToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                productToDelete = null
            },
            title = { Text("상품 삭제") },
            text = { Text("'${productToDelete?.title}' 상품을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    // 상품 삭제 처리
                    productToDelete?.let { product ->
                        viewModel.deleteProduct(product.id)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("상품이 삭제되었습니다.")
                        }
                    }
                    showDeleteDialog = false
                    productToDelete = null
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    productToDelete = null
                }) {
                    Text("취소")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HomeToolbar(
                    onNotificationClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("새로운 알림이 없습니다.")
                        }
                    }
                )

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
                            items(
                                items = products,
                                key = { it.id } // 각 아이템의 고유 키 지정
                            ) { product ->
                                ProductItem(
                                    product = product,
                                    isLiked = likedMap[product.id] ?: false,
                                    likeCount = likeCountMap[product.id] ?: product.likeCount,
                                    onLikeClick = { viewModel.toggleLike(product.id) },
                                    onClick = {
                                        // 최신 좋아요 상태가 반영된 제품 객체 생성
                                        val updatedProduct = product.copy(
                                            isLiked = likedMap[product.id] ?: false,
                                            likeCount = likeCountMap[product.id] ?: product.likeCount
                                        )
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "product",
                                            updatedProduct
                                        )
                                        navController.navigate("product_detail/${product.id}")
                                    },
                                    onLongClick = {
                                        // 길게 클릭 시 삭제 대화상자 표시
                                        productToDelete = product
                                        showDeleteDialog = true
                                    },
                                    // 아이템 애니메이션 적용
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                        .animateItem(
                                            fadeOutSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                )
                            }
                        }
                    }

                    is HomeUiState.Error -> {
                        // 에러 상태 처리 (기존 코드와 동일)
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductItem(
    product: ProductModel,
    isLiked: Boolean,
    likeCount: Int,
    onLikeClick: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            // combinedClickable로 변경하여 일반 클릭과 길게 클릭 모두 처리
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // 기존 내용 유지
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 130.dp)
        ) {
            // 이미지 로딩
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
                    Text(text = "$likeCount")
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