package com.jeongu.barointernapp.data.datasource.local

import com.jeongu.barointernapp.data.model.ProductEntity
import com.jeongu.barointernapp.data.model.toLocal
import com.jeongu.barointernapp.domain.datasource.ProductLocalDataSource
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductLocalDataSource {
    override suspend fun getProducts(): List<ProductEntity> =
        productDao.getProducts().map { it.toData() }

    override suspend fun getProduct(productId: Int): ProductEntity? =
        productDao.getProduct(productId)?.toData()

    override suspend fun saveProducts(products: List<ProductEntity>) {
        productDao.insert(products.map { it.toLocal() })
    }

    override suspend fun deleteProduct(productId: Int) {
        productDao.delete(productId)
    }

    override suspend fun updateLike(productId: Int, isLiked: Boolean) {
        productDao.updateLike(productId, isLiked)
    }
}