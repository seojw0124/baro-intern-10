package com.jeongu.barointernapp.data.model

import com.jeongu.barointernapp.data.datasource.local.LocalMapper

data class SellerLocal(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val mannerTemperature: Double
) : LocalMapper<SellerEntity> {
    override fun toData(): SellerEntity =
        SellerEntity(id, name, profileImageUrl, mannerTemperature)
}

fun SellerEntity.toLocal() = SellerLocal(
    id = id,
    name = name,
    profileImageUrl = profileImageUrl,
    mannerTemperature = mannerTemperature
)