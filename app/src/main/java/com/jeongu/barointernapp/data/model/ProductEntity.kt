package com.jeongu.barointernapp.data.model

import com.jeongu.barointernapp.data.DataMapper
import com.jeongu.barointernapp.domain.model.Product

data class ProductEntity(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val introduction: String,
    val price: Int,
    val tradingPlace: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val seller: SellerEntity
) : DataMapper<Product> {
    override fun toDomain(): Product =
        Product(
            id,
            imageUrl,
            title,
            introduction,
            price,
            tradingPlace,
            likeCount,
            commentCount,
            isLiked,
            seller.toDomain()
        )
}
