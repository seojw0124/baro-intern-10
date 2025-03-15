package com.jeongu.barointernapp.domain.datasource

import com.jeongu.barointernapp.data.model.ProductEntity

interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductEntity>
}