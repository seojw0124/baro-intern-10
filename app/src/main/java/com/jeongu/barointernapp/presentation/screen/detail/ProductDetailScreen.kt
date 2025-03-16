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
import com.jeongu.barointernapp.presentation.component.detail.BottomActionBar
import com.jeongu.barointernapp.presentation.component.detail.DetailToolbar
import com.jeongu.barointernapp.presentation.component.detail.SellerInfoSection
import com.jeongu.barointernapp.presentation.model.ProductDetailState
import com.jeongu.barointernapp.presentation.model.ProductModel
import com.jeongu.barointernapp.presentation.model.SellerModel
import com.jeongu.barointernapp.presentation.theme.Gray300
import com.jeongu.barointernapp.presentation.theme.Gray700
import com.jeongu.barointernapp.presentation.theme.White
import com.jeongu.barointernapp.presentation.viewmodel.detail.ProductDetailViewModel
import java.text.NumberFormat
import java.util.Locale

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
                    // 분리된 DetailToolbar 컴포넌트 사용
                    DetailToolbar(
                        title = stringResource(R.string.label_detail_toolbar_title),
                        onBackClick = {
                            // 뒤로 가기 전에 변경된 좋아요 상태를 전달
                            if (uiState is ProductDetailState.Success) {
                                (uiState as ProductDetailState.Success).product.let { updatedProduct ->
                                    navController.previousBackStackEntry?.savedStateHandle?.set("updatedProduct", updatedProduct)
                                }
                            }
                            navController.popBackStack()
                        }
                    )

                    // 상품 이미지
                    AsyncImage(
                        model = productData.imageUrl,
                        contentDescription = stringResource(R.string.description_product_detail_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f / 4f)
                    )

                    SellerInfoSection(
                        seller = productData.seller,
                        location = productData.tradingPlace
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 2.dp,
                        color = Gray300
                    )

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
