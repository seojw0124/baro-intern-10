package com.jeongu.barointernapp.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongu.barointernapp.SampleProduct
import com.jeongu.barointernapp.Storage
import com.jeongu.barointernapp.domain.usecase.GetProductsUseCase
import com.jeongu.barointernapp.domain.usecase.SaveProductsUseCase
import com.jeongu.barointernapp.presentation.model.HomeUiState
import com.jeongu.barointernapp.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val saveProductsUseCase: SaveProductsUseCase,
) : ViewModel() {
    private val _productList = MutableStateFlow<List<SampleProduct>>(Storage.SampleSampleProductLists)
    val productList: StateFlow<List<SampleProduct>> = _productList

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _likedMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val likedMap: StateFlow<Map<Int, Boolean>> = _likedMap

    init {
        // 앱 시작 시 원격 데이터를 로컬에 저장하고 상품 목록을 가져옴
        viewModelScope.launch {
            loadProducts()
        }
    }

    private suspend fun loadProducts() {
        _uiState.value = HomeUiState.Loading
        try {
            // 먼저 원격 데이터를 로컬에 저장
            saveProductsUseCase()

            // 그 다음 로컬에서 데이터를 가져옴
            getProductsUseCase().collect { result ->
                result.fold(
                    onSuccess = { products ->
                        _uiState.value = HomeUiState.Success(products.toPresentation())
                        Log.e("qwer", "products: $products")
                        // 좋아요 맵 초기화
                        _likedMap.value = products.associate { it.id to it.isLiked }
                    },
                    onFailure = { e ->
                        _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
                        Log.e("qwer", "error: $e")
                    }
                )
            }
        } catch (e: Exception) {
            _uiState.value = HomeUiState.Error(e.message ?: "Unknown error occurred")
            Log.e("qwer", "error: $e")
        }
    }

    fun toggleLike(productId: Int) {
        val current = _likedMap.value[productId] ?: false
        _likedMap.value = _likedMap.value.toMutableMap().apply {
            put(productId, !current)
        }
    }
}