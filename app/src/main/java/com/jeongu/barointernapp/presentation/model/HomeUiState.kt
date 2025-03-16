package com.jeongu.barointernapp.presentation.model

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val products: List<ProductModel>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}