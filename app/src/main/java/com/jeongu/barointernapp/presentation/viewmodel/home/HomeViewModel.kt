package com.jeongu.barointernapp.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongu.barointernapp.SampleProduct
import com.jeongu.barointernapp.Storage
import com.jeongu.barointernapp.domain.usecase.DeleteProductUseCase
import com.jeongu.barointernapp.domain.usecase.GetProductsUseCase
import com.jeongu.barointernapp.domain.usecase.SaveProductsUseCase
import com.jeongu.barointernapp.domain.usecase.UpdateLikeUseCase
import com.jeongu.barointernapp.presentation.model.HomeUiState
import com.jeongu.barointernapp.presentation.model.ProductModel
import com.jeongu.barointernapp.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val saveProductsUseCase: SaveProductsUseCase,
    private val updateLikeUseCase: UpdateLikeUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _likedMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val likedMap = _likedMap.asStateFlow()

    // 제품 목록을 캐시하는 변수 추가
    private var cachedProducts = listOf<ProductModel>()

    // 좋아요 수를 트래킹하는 맵 추가
    private val _likeCountMap = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val likeCountMap = _likeCountMap.asStateFlow()

    // 최초 로드 여부를 추적하는 변수
    private var isFirstLoad = true

    init {
        // 앱 시작 시 상품 목록을 가져옴
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                if (isFirstLoad) {
                    try {
                        saveProductsUseCase()
                        isFirstLoad = false
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Failed to save remote data: ${e.message}")
                    }
                }

                getProductsUseCase().collect { result ->
                    result.fold(
                        onSuccess = { products ->
                            cachedProducts = products.toPresentation()
                            _uiState.value = HomeUiState.Success(products.toPresentation())
                            _likedMap.value = products.associate { it.id to it.isLiked }
                            _likeCountMap.value = products.associate { it.id to it.likeCount }
                        },
                        onFailure = { e ->
                            _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
                            Log.e("HomeViewModel", "Failed to get products: ${e.message}")
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
                Log.e("HomeViewModel", "Error in loadProducts: ${e.message}")
            }
        }
    }

    fun toggleLike(productId: Int) {
        viewModelScope.launch {
            // 현재 좋아요 상태의 반대값
            val currentLiked = _likedMap.value[productId] ?: false
            val newLikedState = !currentLiked

            // 현재 좋아요 수
            val currentLikeCount = _likeCountMap.value[productId] ?: 0

            // 좋아요 수 변화 계산 (좋아요 추가면 +1, 제거면 -1)
            val delta = if (newLikedState) 1 else -1
            val newLikeCount = currentLikeCount + delta

            // 낙관적 업데이트: UI 즉시 변경
            _likedMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[productId] = newLikedState
                }
            }

            // 좋아요 수도 즉시 업데이트
            _likeCountMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[productId] = newLikeCount
                }
            }

            try {
                // 백그라운드에서 저장소 업데이트
                updateLikeUseCase(productId, newLikedState)
            } catch (e: Exception) {
                // 실패 시 원래 상태로 복원
                _likedMap.update { currentMap ->
                    currentMap.toMutableMap().apply {
                        this[productId] = currentLiked
                    }
                }

                // 좋아요 수도 원래대로 복원
                _likeCountMap.update { currentMap ->
                    currentMap.toMutableMap().apply {
                        this[productId] = currentLikeCount
                    }
                }
                Log.e("HomeViewModel", "Failed to update like: ${e.message}")
            }
        }
    }

    fun updateProductInList(updatedProduct: ProductModel) {
        viewModelScope.launch {
            // 좋아요 상태 업데이트
            _likedMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[updatedProduct.id] = updatedProduct.isLiked
                }
            }

            // 좋아요 수 업데이트
            _likeCountMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[updatedProduct.id] = updatedProduct.likeCount
                }
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                // 로컬 DB에서 상품 삭제
                deleteProductUseCase(productId)

                // UI 상태 업데이트 (성공 상태일 경우에만)
                if (_uiState.value is HomeUiState.Success) {
                    val currentProducts = (_uiState.value as HomeUiState.Success).products
                    val updatedProducts = currentProducts.filter { it.id != productId }
                    _uiState.value = HomeUiState.Success(updatedProducts)

                    // 좋아요 맵에서도 제거
                    _likedMap.update { currentMap ->
                        currentMap.toMutableMap().apply {
                            remove(productId)
                        }
                    }

                    // 좋아요 수 맵에서도 제거
                    _likeCountMap.update { currentMap ->
                        currentMap.toMutableMap().apply {
                            remove(productId)
                        }
                    }
                }
            } catch (e: Exception) {
                // 삭제 중 오류 발생 시 처리
                Log.e("HomeViewModel", "Failed to delete product: ${e.message}")
            }
        }
    }
}