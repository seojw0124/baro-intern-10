package com.jeongu.barointernapp.data.model

import com.google.gson.annotations.SerializedName
import com.jeongu.barointernapp.data.datasource.remote.RemoteMapper

data class SellerResponse(
    val id: Int,
    val name: String,
    @SerializedName("profile_image_url") val profileImageUrl: String,
    @SerializedName("manner_temperature") val mannerTemperature: Double
) : RemoteMapper<SellerEntity> {

    override fun toData(): SellerEntity =
        SellerEntity(id, name, profileImageUrl, mannerTemperature)

}