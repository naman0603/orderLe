package com.example.orderleapp.dataModel

import com.google.gson.annotations.SerializedName

data class ProductDetailBean(
    @SerializedName("product_id")
    val productId: String
)