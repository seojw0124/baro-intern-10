package com.jeongu.barointernapp.presentation.model

import android.os.Parcelable
import com.jeongu.barointernapp.domain.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val introduction: String,
    val price: Int,
    val tradingPlace: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val seller: SellerModel
) : Parcelable

fun Product.toPresentation(): ProductModel = ProductModel(
    id = id,
    imageUrl = imageUrl,
    title = title,
    introduction = introduction,
    price = price,
    tradingPlace = tradingPlace,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    seller = seller.toPresentation()
)

fun List<Product>.toPresentation(): List<ProductModel> = map {
    it.toPresentation()
}
