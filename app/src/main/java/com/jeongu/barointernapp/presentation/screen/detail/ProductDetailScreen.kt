package com.jeongu.barointernapp.presentation.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.jeongu.barointernapp.presentation.theme.Gray300
import com.jeongu.barointernapp.presentation.viewmodel.detail.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val product = navController.previousBackStackEntry?.savedStateHandle?.get<ProductModel>("product")

    LaunchedEffect(Unit) {
        product?.let { viewModel.setProduct(it) }
    }

    val uiState by viewModel.uiState.collectAsState()

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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 80.dp)
                ) {
                    DetailToolbar(
                        title = stringResource(R.string.label_detail_toolbar_title),
                        onBackClick = {
                            (uiState as ProductDetailState.Success).product.let { updatedProduct ->
                                navController.previousBackStackEntry?.savedStateHandle?.set("updatedProduct", updatedProduct)
                            }
                            navController.popBackStack()
                        }
                    )

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
