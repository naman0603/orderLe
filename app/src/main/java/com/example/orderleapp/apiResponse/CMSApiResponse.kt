package com.example.orderleapp.apiResponse

import com.google.gson.annotations.SerializedName

data class CMSApiResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("page_id")
    val pageId: String,
    @SerializedName("page_name")
    val pageName: String,
    @SerializedName("page_content")
    val pageContent: String
)
