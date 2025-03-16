package com.jeongu.barointernapp.domain.usecase

import com.jeongu.barointernapp.domain.model.Product
import com.jeongu.barointernapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Flow<Result<Product>> {
        return productRepository.getProductById(productId)
    }
}