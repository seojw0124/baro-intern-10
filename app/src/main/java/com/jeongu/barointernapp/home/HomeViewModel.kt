package com.jeongu.barointernapp.home

import androidx.lifecycle.ViewModel
import com.jeongu.barointernapp.SampleProduct
import com.jeongu.barointernapp.Storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _productList = MutableStateFlow<List<SampleProduct>>(Storage.SampleSampleProductLists)
    val productList: StateFlow<List<SampleProduct>> = _productList

    private val _likedMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val likedMap: StateFlow<Map<Int, Boolean>> = _likedMap

    fun toggleLike(productId: Int) {
        val current = _likedMap.value[productId] ?: false
        _likedMap.value = _likedMap.value.toMutableMap().apply {
            put(productId, !current)
        }
    }
}