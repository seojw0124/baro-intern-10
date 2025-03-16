package com.jeongu.barointernapp.data.repository

import com.jeongu.barointernapp.domain.datasource.ProductLocalDataSource
import com.jeongu.barointernapp.domain.datasource.ProductRemoteDataSource
import com.jeongu.barointernapp.domain.model.Product
import com.jeongu.barointernapp.domain.model.toDomain
import com.jeongu.barointernapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
) : ProductRepository {

    override suspend fun getProducts(): Flow<Result<List<Product>>> = flow {
        emit(Result.success(productLocalDataSource.getProducts().toDomain()))
    }

    override suspend fun getProductById(productId: Int): Flow<Result<Product>> = flow {
        emit(Result.success(productLocalDataSource.getProductById(productId)?.toDomain() ?: throw Exception("Product not found")))
    }

    override suspend fun saveProducts() {
        val products = productRemoteDataSource.getProducts()
        productLocalDataSource.saveProducts(products)
    }

    override suspend fun deleteProduct(productId: Int) {
        productLocalDataSource.deleteProduct(productId)
    }

    override suspend fun updateLike(productId: Int, isLiked: Boolean, likeDelta: Int) {
        productLocalDataSource.updateLike(productId, isLiked, likeDelta)
    }
}