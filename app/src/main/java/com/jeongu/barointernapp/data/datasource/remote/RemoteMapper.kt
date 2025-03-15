package com.jeongu.barointernapp.data.datasource.remote

interface RemoteMapper<DataModel> {
    fun toData(): DataModel
}