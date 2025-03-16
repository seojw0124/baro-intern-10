package com.jeongu.barointernapp.data

internal interface DataMapper<DomainModel> {
    fun toDomain(): DomainModel
}