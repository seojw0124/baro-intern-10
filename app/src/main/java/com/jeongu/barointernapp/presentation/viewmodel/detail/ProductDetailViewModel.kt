package com.jeongu.barointernapp.presentation.viewmodel.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
    // 상품 상세 상태 관리
    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState = _uiState.asStateFlow()

    // 이미 설정되었는지 추적
    private var productAlreadySet = false

    // 상품 정보 설정 (중복 설정 방지)
    fun setProduct(product: ProductModel) {
        if (!productAlreadySet) {
            _uiState.value = ProductDetailState.Success(product)
            productAlreadySet = true
        }
    }

    // 좋아요 토글 함수
    fun toggleLike() {
        val currentState = _uiState.value
        if (currentState is ProductDetailState.Success) {
            val product = currentState.product
            val updatedProduct = product.copy(
                isLiked = !product.isLiked,
                likeCount = if (!product.isLiked) product.likeCount + 1 else product.likeCount - 1
            )

            // 낙관적 UI 업데이트
            _uiState.value = ProductDetailState.Success(updatedProduct)

            // 백엔드 업데이트 (Room DB)
            viewModelScope.launch {
                try {
                    updateLikeUseCase(updatedProduct.id, updatedProduct.isLiked)
                } catch (e: Exception) {
                    // 실패 시 원래 상태로 복구
                    _uiState.value = ProductDetailState.Success(product)
                    Log.e("ProductDetailViewModel", "Failed to update like: ${e.message}")
                }
            }
        }
    }
}