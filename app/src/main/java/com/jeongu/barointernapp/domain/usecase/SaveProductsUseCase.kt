package com.jeongu.barointernapp.domain.usecase

import com.jeongu.barointernapp.domain.repository.ProductRepository
import javax.inject.Inject

class SaveProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke() {
        productRepository.saveProducts()
    }
}