package com.jeongu.barointernapp.domain.usecase

import com.jeongu.barointernapp.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateLikeUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, isLiked: Boolean) {
        val delta = if (isLiked) 1 else -1
        productRepository.updateLike(productId, isLiked, delta)
    }
}