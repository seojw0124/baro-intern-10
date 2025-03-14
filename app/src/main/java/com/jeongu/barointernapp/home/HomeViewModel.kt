package com.jeongu.barointernapp.home

import androidx.lifecycle.ViewModel
import com.jeongu.barointernapp.Product
import com.jeongu.barointernapp.Storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>>(Storage.productList)
    val productList: StateFlow<List<Product>> = _productList
}