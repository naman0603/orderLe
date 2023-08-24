package com.example.orderleapp.dataModel

import com.google.gson.annotations.SerializedName


data class ProductTypeBean(
    @SerializedName("product_image")
    val productImage: String,
    @SerializedName("media_type")
    val mediaType: Int,
    @SerializedName("video_frame")
    val videoFrame: String
)
