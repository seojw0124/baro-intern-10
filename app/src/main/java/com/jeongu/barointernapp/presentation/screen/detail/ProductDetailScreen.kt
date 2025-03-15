package com.jeongu.barointernapp.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jeongu.barointernapp.R
import com.jeongu.barointernapp.presentation.model.ProductDetailState
import com.jeongu.barointernapp.presentation.model.ProductModel
import com.jeongu.barointernapp.presentation.model.SellerModel
import com.jeongu.barointernapp.presentation.theme.Gray300
import com.jeongu.barointernapp.presentation.theme.Gray700
import com.jeongu.barointernapp.presentation.theme.White
import com.jeongu.barointernapp.presentation.viewmodel.detail.ProductDetailViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val product = navController.previousBackStackEntry?.savedStateHandle?.get<ProductModel>("product")
    Log.e("asdf", "product: $product")

    // 제품 상태가 있으면 ViewModel에 설정
    LaunchedEffect(Unit) {
        product?.let { viewModel.setProduct(it) }
    }

    // ViewModel에서 좋아요 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 스크롤 상태 관리
    val scrollState = rememberScrollState()
    val isScrollable = scrollState.maxValue > 0
    val scrollBehavior = if (isScrollable) {
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    } else {
        TopAppBarDefaults.pinnedScrollBehavior()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상품 상세") },
                navigationIcon = {
                    IconButton(onClick = {
                        // 뒤로 가기 전에 변경된 좋아요 상태를 전달
                        (uiState as ProductDetailState.Success).product.let { updatedProduct ->
                            navController.previousBackStackEntry?.savedStateHandle?.set("updatedProduct", updatedProduct)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    scrolledContainerColor = White.copy(alpha = 0.95f)
                ),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        when (uiState) {
            is ProductDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductDetailState.Success -> {
                val productData = (uiState as ProductDetailState.Success).product
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 스크롤 가능한 콘텐츠 영역
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(bottom = 80.dp)
                    ) {
                        // 상품 이미지
                        AsyncImage(
                            model = productData.imageUrl,
                            contentDescription = stringResource(R.string.description_product_detail_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(5f / 4f)
                        )

                        // 판매자 정보 영역
                        SellerInfoSection(
                            seller = productData.seller,
                            location = productData.tradingPlace
                        )

                        // 구분선
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 2.dp,
                            color = Gray300
                        )

                        // 상품 정보 영역
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 18.dp)
                        ) {
                            Text(
                                text = productData.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = productData.introduction,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // 하단 액션바 (고정 위치)
                    BottomActionBar(
                        price = productData.price,
                        isLiked = productData.isLiked, // 현재 제품의 좋아요 상태 사용
                        onLikeClick = { viewModel.toggleLike() }, // 좋아요 토글 함수 호출
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            is ProductDetailState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = (uiState as ProductDetailState.Error).message)
                }
            }
        }
    }
}

@Composable
fun SellerInfoSection(
    seller: SellerModel,
    location: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            AsyncImage(
                model = seller.profileImageUrl,
                contentDescription = stringResource(id = R.string.description_seller_profile_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            )

            // 판매자 이름 및 위치 정보
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = seller.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Gray700
                    )
                )
            }

            // 매너 온도
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${seller.mannerTemperature} °C",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = getTemperatureColor(seller.mannerTemperature)
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(id = R.string.label_manner_temperature),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Gray300
                    )
                )
            }
        }
    }
}

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
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 좋아요 버튼
            Box(
                modifier = Modifier.width(48.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        painter = painterResource(
                            id = if (isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outlined
                        ),
                        contentDescription = "좋아요",
                        tint = if (isLiked) Color.Red else Color.Unspecified
                    )
                }
            }

            // 가격 정보
            Text(
                text = stringResource(
                    R.string.format_prodcut_price,
                    NumberFormat.getNumberInstance(Locale.KOREA).format(price)
                ),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )

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

// 매너 온도에 따른 색상 변경 함수
@Composable
fun getTemperatureColor(temperature: Double): Color {
    return when {
        temperature >= 70 -> Color(0xFF00B493) // 높음 - 초록색
        temperature >= 50 -> Color(0xFF2E7D32) // 중간 - 진한 초록
        temperature >= 30 -> Color(0xFFFF9800) // 낮음 - 주황색
        else -> Color(0xFFE53935) // 매우 낮음 - 빨간색
    }
}

