package com.jeongu.barointernapp.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongu.barointernapp.SampleProduct

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val productList by viewModel.sampleProductList.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(productList) { product ->
            ProductItem(sampleProduct = product, onClick = {
                navController.navigate("product_detail/${product.id}")
            })
        }
    }
}

@Composable
fun ProductItem(sampleProduct: SampleProduct, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = sampleProduct.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "${sampleProduct.price}원", style = MaterialTheme.typography.bodyMedium)
        }
    }
}