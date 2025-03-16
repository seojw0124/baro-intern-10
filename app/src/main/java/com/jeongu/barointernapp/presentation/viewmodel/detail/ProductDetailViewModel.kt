package com.jeongu.barointernapp.presentation.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongu.barointernapp.domain.usecase.UpdateLikeUseCase
import com.jeongu.barointernapp.presentation.model.ProductDetailState
import com.jeongu.barointernapp.presentation.model.ProductModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val updateLikeUseCase: UpdateLikeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    private var productAlreadySet = false

    fun setProduct(product: ProductModel) {
        if (!productAlreadySet) {
            _uiState.value = ProductDetailState.Success(product)
            productAlreadySet = true
        }
    }

    fun toggleLike() {
        val currentState = _uiState.value
        if (currentState is ProductDetailState.Success) {
            val product = currentState.product
            val updatedProduct = product.copy(
                isLiked = !product.isLiked,
                likeCount = if (!product.isLiked) product.likeCount + 1 else product.likeCount - 1
            )

            _uiState.value = ProductDetailState.Success(updatedProduct)

            viewModelScope.launch {
                try {
                    updateLikeUseCase(updatedProduct.id, updatedProduct.isLiked)
                } catch (e: Exception) {
                    _uiState.value = ProductDetailState.Success(product)
                }
            }
        }
    }
}