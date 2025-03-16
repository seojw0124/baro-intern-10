package com.jeongu.barointernapp.domain.model

import com.jeongu.barointernapp.data.model.ProductEntity
import com.jeongu.barointernapp.data.model.SellerEntity

data class Product(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val introduction: String,
    val price: Int,
    val tradingPlace: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val seller: Seller
)

//fun List<ProductEntity>.toDomain(): List<Product> = map {
//    it.toDomain()
//}

//fun ProductEntity.toDomain(): Product = Product(
//    id = id,
//    imageUrl = imageUrl,
//    title = title,
//    introduction = introduction,
//    price = price,
//    tradingPlace = tradingPlace,
//    likeCount = likeCount,
//    commentCount = commentCount,
//    isLiked = isLiked,
//    seller = Seller(
//        id = seller.id,
//        name = seller.name,
//        profileImageUrl = seller.profileImageUrl,
//        mannerTemperature = seller.mannerTemperature
//    )
//)
