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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is ProductDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductDetailState.Success -> {
                val productData = (uiState as ProductDetailState.Success).product

                // 스크롤 가능한 콘텐츠 영역 (TopAppBar 포함)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 80.dp) // 하단 액션바 높이만큼 패딩
                ) {
                    // 커스텀 TopAppBar - 스크롤 영역 내에 포함
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            // 뒤로 가기 전에 변경된 좋아요 상태를 전달
                            if (uiState is ProductDetailState.Success) {
                                (uiState as ProductDetailState.Success).product.let { updatedProduct ->
                                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedProduct", updatedProduct)
                                }
                            }
                            navController.popBackStack()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "뒤로가기"
                            )
                        }

                        Text(
                            text = "상품 상세",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

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
                    isLiked = productData.isLiked,
                    onLikeClick = { viewModel.toggleLike() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            is ProductDetailState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                    text = stringResource(
                        R.string.format_manner_temperature,
                        seller.mannerTemperature
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = getTemperatureColor(seller.mannerTemperature)
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(id = R.string.label_manner_temperature),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Gray300
                    ),
                    modifier = Modifier.drawWithContent {
                        drawContent()
                        // 텍스트 아래에 2dp 높이의 밑줄 그리기
                        drawRect(
                            color = Gray300,
                            topLeft = Offset(0f, size.height - 2.dp.toPx()),
                            size = Size(size.width, 2.dp.toPx())
                        )
                    }
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

