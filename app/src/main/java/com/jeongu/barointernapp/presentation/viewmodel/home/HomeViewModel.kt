package com.jeongu.barointernapp.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongu.barointernapp.domain.usecase.DeleteProductUseCase
import com.jeongu.barointernapp.domain.usecase.GetProductsUseCase
import com.jeongu.barointernapp.domain.usecase.SaveProductsUseCase
import com.jeongu.barointernapp.domain.usecase.UpdateLikeUseCase
import com.jeongu.barointernapp.presentation.model.HomeUiState
import com.jeongu.barointernapp.presentation.model.ProductModel
import com.jeongu.barointernapp.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private var cachedProducts = listOf<ProductModel>()

    private val _likeCountMap = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val likeCountMap = _likeCountMap.asStateFlow()

    private var isFirstLoad = true

    init {
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
            val currentLiked = _likedMap.value[productId] ?: false
            val newLikedState = !currentLiked

            val currentLikeCount = _likeCountMap.value[productId] ?: 0

            val delta = if (newLikedState) 1 else -1
            val newLikeCount = currentLikeCount + delta

            _likedMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[productId] = newLikedState
                }
            }

            _likeCountMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[productId] = newLikeCount
                }
            }

            try {
                updateLikeUseCase(productId, newLikedState)
            } catch (e: Exception) {
                _likedMap.update { currentMap ->
                    currentMap.toMutableMap().apply {
                        this[productId] = currentLiked
                    }
                }

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
            _likedMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[updatedProduct.id] = updatedProduct.isLiked
                }
            }

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
                deleteProductUseCase(productId)

                if (_uiState.value is HomeUiState.Success) {
                    val currentProducts = (_uiState.value as HomeUiState.Success).products
                    val updatedProducts = currentProducts.filter { it.id != productId }
                    _uiState.value = HomeUiState.Success(updatedProducts)

                    _likedMap.update { currentMap ->
                        currentMap.toMutableMap().apply {
                            remove(productId)
                        }
                    }

                    _likeCountMap.update { currentMap ->
                        currentMap.toMutableMap().apply {
                            remove(productId)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to delete product: ${e.message}")
            }
        }
    }
}