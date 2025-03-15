package com.jeongu.barointernapp.domain.usecase

import com.jeongu.barointernapp.domain.model.Product
import com.jeongu.barointernapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Flow<Result<List<Product>>> {
        return productRepository.getProducts()
    }
}