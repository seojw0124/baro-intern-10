package com.jeongu.barointernapp.presentation.model

sealed interface ProductDetailState {
    data object Loading : ProductDetailState
    data class Success(val product: ProductModel) : ProductDetailState
    data class Error(val message: String) : ProductDetailState
}