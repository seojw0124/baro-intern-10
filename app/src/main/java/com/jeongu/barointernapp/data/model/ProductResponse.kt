package com.jeongu.barointernapp.data.model

import com.google.gson.annotations.SerializedName
import com.jeongu.barointernapp.data.datasource.remote.RemoteMapper

data class ProductResponse(
    val id: Int,
    @SerializedName("image_url") val imageUrl: String,
    val title: String,
    val introduction: String,
    val price: Int,
    @SerializedName("trading_place") val tradingPlace: String,
    @SerializedName("like_count") val likeCount: Int,
    @SerializedName("comment_count") val commentCount: Int,
    val isLiked: Boolean,
    val seller: SellerResponse
) : RemoteMapper<ProductEntity> {

    override fun toData(): ProductEntity =
        ProductEntity(
            id = id,
            imageUrl = imageUrl,
            title = title,
            introduction = introduction,
            price = price,
            tradingPlace = tradingPlace,
            likeCount = likeCount,
            commentCount = commentCount,
            isLiked = isLiked,
            seller = seller.toData()
        )

}