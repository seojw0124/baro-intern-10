package com.jeongu.barointernapp.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeongu.barointernapp.Storage

@Composable
fun ProductDetailScreen(productId: Int, viewModel: ProductDetailViewModel = hiltViewModel()) {
    val product by viewModel.getProductById(productId).collectAsState(initial = null)
    product?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = it.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "가격: ${it.price}원")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it.description)
        }
    }
}

// Preview
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(productId = 0)
}

