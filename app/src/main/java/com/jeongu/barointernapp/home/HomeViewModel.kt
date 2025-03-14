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
    private val _Sample_productList = MutableStateFlow<List<SampleProduct>>(Storage.SampleSampleProductLists)
    val sampleProductList: StateFlow<List<SampleProduct>> = _Sample_productList
}