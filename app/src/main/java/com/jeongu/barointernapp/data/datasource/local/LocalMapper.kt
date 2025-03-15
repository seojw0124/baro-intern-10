package com.jeongu.barointernapp.data.datasource.local

interface LocalMapper<DataModel> {
    fun toData(): DataModel
}