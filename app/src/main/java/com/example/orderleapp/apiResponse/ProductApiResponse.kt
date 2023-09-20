package com.example.orderleapp.apiResponse

import com.example.orderleapp.dataModel.ProductDetailBean
import com.example.orderleapp.dataModel.ProductTypeBean
import com.google.gson.annotations.SerializedName

data class ProductApiResponse(
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("product_category_id")
    val productCategoryId: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_description")
    val productDescription: String,
    @SerializedName("product_picture_url")
    val productPictureUrl: String,
    @SerializedName("product_picture")
    val productPicture: String,
    @SerializedName("product_weight")
    val productWeight: Double,
    @SerializedName("created_date")
    val createdDate: String,
    val wastage: String,
    @SerializedName("stone_charge")
    val stoneCharge: String,
    @SerializedName("total_stone_charge")
    val totalStoneCharge: Int,
    @SerializedName("category_title")
    val categoryTitle: String,
    @SerializedName("item_code")
    val itemCode: String,
    @SerializedName("request_master_id")
    val requestMasterId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("request_number")
    val requestNumber: String,
    @SerializedName("product_request_id")
    val productRequestId: Int,
    val quantity: Int,
    @SerializedName("party_name")
    val partyName: String,
    @SerializedName("party_email")
    val partyEmail: String,
    @SerializedName("party_phone")
    val partyPhone: String,
    @SerializedName("order_status")
    val orderStatus: Int,
    @SerializedName("invoice_file")
    val invoiceFile: String,
    val qty: Int = 1,
    @SerializedName("product_total_weight")
    val productTotalWeight: Double,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("ringSize")
    val ringSize: String,
    val isChecked: Boolean,
    var ringSizeArray: Array<CharSequence>,
    @SerializedName("order_description")
    val orderDescription: String,
    @SerializedName("gold_type")
    var goldType: String,
    @SerializedName("goldtype_id")
    var goldTypeId: Int,
    @SerializedName("gold_carat")
    val goldCarat: String,
    var productTypeBeans: ArrayList<ProductTypeBean>,
    var productDetailBeans: ArrayList<ProductDetailBean>
) {

}

