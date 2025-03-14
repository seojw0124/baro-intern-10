package com.jeongu.barointernapp.detail

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongu.barointernapp.R
import com.jeongu.barointernapp.Storage
import com.jeongu.barointernapp.ui.theme.Gray100
import com.jeongu.barointernapp.ui.theme.Gray300
import com.jeongu.barointernapp.ui.theme.Gray500
import com.jeongu.barointernapp.ui.theme.Gray700
import com.jeongu.barointernapp.ui.theme.Green
import com.jeongu.barointernapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val product by viewModel.getProductById(productId).collectAsState(initial = null)

    // 스크롤 상태 관리
    val scrollState = rememberScrollState()

    // 콘텐츠가 스크롤 가능한지 여부를 확인
    val isScrollable = scrollState.maxValue > 0

    // 스크롤 가능한 경우에만 enterAlwaysScrollBehavior 적용, 아닌 경우 pinnedScrollBehavior 적용
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
                    IconButton(onClick = { navController.popBackStack() }) {
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
        product?.let { productData ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Scaffold의 내부 패딩 적용
            ) {
                // 스크롤 가능한 콘텐츠 영역
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState) // 이전 선언한 scrollState 사용
                        .padding(bottom = 80.dp) // 하단 액션바 높이만큼 패딩
                ) {
                    // 상품 이미지
                    Image(
                        painter = painterResource(id = productData.image),
                        contentDescription = stringResource(R.string.description_product_detail_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f / 4f) // 5:4 비율 유지
                    )

                    // 판매자 정보 영역
                    SellerInfoSection(
                        sellerName = productData.sellerName,
                        sellerLocation = productData.tradingPlace,
                        mannerTemperature = 36.5f
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
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun SellerInfoSection(
    sellerName: String,
    sellerLocation: String,
    mannerTemperature: Float
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
            Image(
                painter = painterResource(id = R.drawable.img_profile),
                contentDescription = stringResource(id = R.string.description_seller_profile_image),
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
                    text = sellerName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = sellerLocation,
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
                    text = "$mannerTemperature °C",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Green
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
fun BottomActionBar(modifier: Modifier = Modifier) {
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 좋아요 버튼
            IconButton(onClick = { /* 좋아요 기능 */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_like_outlined), // drawable 폴더의 ic_favorite 이미지 사용
                    contentDescription = "좋아요"
                )
            }


            // 가격 정보
            Text(
                text = "39,000원",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
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
