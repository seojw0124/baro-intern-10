package com.jeongu.barointernapp.data.model

import com.jeongu.barointernapp.data.DataMapper
import com.jeongu.barointernapp.domain.model.Seller

data class SellerEntity(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val mannerTemperature: Double
) : DataMapper<Seller> {
    override fun toDomain(): Seller =
        Seller(id, name, profileImageUrl, mannerTemperature)
}