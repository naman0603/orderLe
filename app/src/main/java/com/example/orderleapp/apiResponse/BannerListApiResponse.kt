package com.example.orderleapp.apiResponse

import com.google.gson.annotations.SerializedName


data class BannerListApiResponse(
    @SerializedName("banner_id")
    val bannerId: String,
    @SerializedName("banner")
    val banner: String,
    @SerializedName("created_date")
    val createdDate: String
)
