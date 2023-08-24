package com.example.orderleapp.apiResponse

import com.google.gson.annotations.SerializedName

data class LoginApiResponse(
    @SerializedName("data")
    val data: ApiData?,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)

data class ApiData(
    @SerializedName("user_data")
    val userData: List<UserData>,
    @SerializedName("branding_data")
    val brandingData: List<BrandingData>
)

data class UserData(
    @SerializedName("company_id")
    val companyId: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("device_unique_id")
    val deviceUniqueId: String,
    @SerializedName("device_token")
    val deviceToken: String,
    @SerializedName("login_access_token")
    val loginAccessToken: String,
    @SerializedName("created_date")
    val createdDate: String
)

data class BrandingData(
    @SerializedName("id")
    val id: String,
    @SerializedName("company_id")
    val companyId: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("branding_logo")
    val brandingLogo: String,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("gst")
    val gst: String,
    @SerializedName("cin")
    val cin: String,
    @SerializedName("pan")
    val pan: String,
    @SerializedName("from_email_address")
    val fromEmailAddress: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    @SerializedName("logo_url")
    val logoUrl: String
)
