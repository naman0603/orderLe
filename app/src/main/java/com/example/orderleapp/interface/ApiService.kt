package com.example.orderleapp.`interface`

import com.example.orderleapp.apiResponse.BannerListApiResponse
import com.example.orderleapp.apiResponse.LoginApiResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("userLogin")
    fun login(
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("device_unique_id") deviceUniqueId: String,
        @Field("device_token") deviceToken: String,
        @Field("device_type") deviceType: String
    ): Call<LoginApiResponse>

    @FormUrlEncoded
    @POST("getBanners")
    fun bannerList(
        @Field("user_id") user_id: String,
        @Field("code") code: String,
        @Field("login_access_token") login_access_token: String,

    ): Call<BannerListApiResponse>

}
