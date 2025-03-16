package com.jeongu.barointernapp.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongu.barointernapp.R
import com.jeongu.barointernapp.presentation.component.home.DeleteDialog
import com.jeongu.barointernapp.presentation.component.home.HomeToolbar
import com.jeongu.barointernapp.presentation.component.home.ProductItem
import com.jeongu.barointernapp.presentation.component.home.ScrollToTopButton
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

    val snackbarHostState = remember { SnackbarHostState() }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    val updatedProduct = navController.currentBackStackEntry?.savedStateHandle?.get<ProductModel>("updatedProduct")

    LaunchedEffect(updatedProduct) {
        updatedProduct?.let {
            viewModel.updateProductInList(it)
        }
    }

    if (showDeleteDialog && productToDelete != null) {
        DeleteDialog(
            product = productToDelete!!,
            onConfirm = {
                viewModel.deleteProduct(productToDelete!!.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("상품이 삭제되었습니다.")
                }
                showDeleteDialog = false
                productToDelete = null
            },
            onDismiss = {
                showDeleteDialog = false
                productToDelete = null
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
                            snackbarHostState.showSnackbar(context.getString(R.string.snackbar_no_notification))
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
                                key = { it.id }
                            ) { product ->
                                ProductItem(
                                    product = product,
                                    isLiked = likedMap[product.id] ?: false,
                                    likeCount = likeCountMap[product.id] ?: product.likeCount,
                                    onLikeClick = { viewModel.toggleLike(product.id) },
                                    onClick = {
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
                                        productToDelete = product
                                        showDeleteDialog = true
                                    },
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
                        ErrorContent(
                            message = (uiState as HomeUiState.Error).message,
                            onRetry = { viewModel.loadProducts() }
                        )
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

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Button(onClick = onRetry) {
                Text("다시 시도")
            }
        }
    }
}