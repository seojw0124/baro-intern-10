package com.jeongu.barointernapp.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongu.barointernapp.SampleProduct
import com.jeongu.barointernapp.Storage
import com.jeongu.barointernapp.domain.usecase.GetProductsUseCase
import com.jeongu.barointernapp.domain.usecase.SaveProductsUseCase
import com.jeongu.barointernapp.domain.usecase.UpdateLikeUseCase
import com.jeongu.barointernapp.presentation.model.HomeUiState
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
    private val updateLikeUseCase: UpdateLikeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _likedMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val likedMap = _likedMap.asStateFlow()

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
                // 최초 로드 시에만 원격 데이터를 로컬에 저장
                if (isFirstLoad) {
                    try {
                        saveProductsUseCase()
                        isFirstLoad = false
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Failed to save remote data: ${e.message}")
                        // 원격 데이터 저장 실패해도 로컬 데이터 로드는 시도
                    }
                }

                // 로컬에서 데이터를 가져옴
                getProductsUseCase().collectLatest { result ->
                    result.fold(
                        onSuccess = { products ->
                            _uiState.value = HomeUiState.Success(products.toPresentation())
                            // 좋아요 맵 초기화
                            _likedMap.value = products.associate { it.id to it.isLiked }
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

            // 낙관적 업데이트: UI 즉시 변경
            _likedMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    this[productId] = newLikedState
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
                // 오류 메시지 처리 (필요 시)
            }
        }
    }
}