package com.jeongu.barointernapp.domain.repository

import com.jeongu.barointernapp.data.model.ProductEntity
import com.jeongu.barointernapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(): Flow<Result<List<Product>>>

    suspend fun getProduct(productId: Int): Flow<Result<Product>>

    suspend fun saveProducts()

    suspend fun deleteProduct(productId: Int)

    suspend fun updateLike(productId: Int, isLiked: Boolean, delta: Int)
}