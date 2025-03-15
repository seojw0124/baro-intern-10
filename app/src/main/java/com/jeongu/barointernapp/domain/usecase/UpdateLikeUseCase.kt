package com.jeongu.barointernapp.domain.usecase

import com.jeongu.barointernapp.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateLikeUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, isLiked: Boolean) {
        productRepository.updateLike(productId, isLiked)
    }
}