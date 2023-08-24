package com.example.orderleapp.apiResponse

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MyOrdersApiResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("request_master_id")
    val requestMasterId: Int,
    @SerializedName("request_number")
    val requestNumber: String,
    @SerializedName("created_date")
    val createdDate: String,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_title")
    val categoryTitle: String,
    @SerializedName("category_picture_url")
    val categoryPictureUrl: String,
    @SerializedName("category_picture")
    val categoryPicture: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(requestMasterId)
        parcel.writeString(requestNumber)
        parcel.writeString(createdDate)
        parcel.writeInt(categoryId)
        parcel.writeString(categoryTitle)
        parcel.writeString(categoryPictureUrl)
        parcel.writeString(categoryPicture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyOrdersApiResponse> {
        override fun createFromParcel(parcel: Parcel): MyOrdersApiResponse {
            return MyOrdersApiResponse(parcel)
        }

        override fun newArray(size: Int): Array<MyOrdersApiResponse?> {
            return arrayOfNulls(size)
        }
    }
}
