package com.jeongu.barointernapp.domain.datasource

import com.jeongu.barointernapp.data.model.ProductEntity

interface ProductLocalDataSource {

    suspend fun getProducts(): List<ProductEntity>

    suspend fun getProduct(productId: Int): ProductEntity?

    suspend fun saveProducts(products: List<ProductEntity>)

    suspend fun deleteProduct(productId: Int)

    suspend fun updateLike(productId: Int, isLiked: Boolean, likeDelta: Int)
}