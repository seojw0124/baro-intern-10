package com.jeongu.barointernapp.detail

import androidx.lifecycle.ViewModel
import com.jeongu.barointernapp.SampleProduct
import com.jeongu.barointernapp.Storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor() : ViewModel() {
    fun getProductById(productId: Int): Flow<SampleProduct?> = flow {
        emit(Storage.SampleSampleProductLists.find { it.id == productId })
    }
}