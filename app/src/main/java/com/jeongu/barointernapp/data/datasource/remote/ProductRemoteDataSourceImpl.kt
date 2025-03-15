package com.jeongu.barointernapp.data.datasource.remote

import com.jeongu.barointernapp.data.model.ProductEntity
import com.jeongu.barointernapp.domain.datasource.ProductRemoteDataSource
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(
    private val apiService: FakeApiService
) : ProductRemoteDataSource {

    override suspend fun getProducts(): List<ProductEntity> {
        return apiService.getProducts().products.map { it.toData() }
    }
}