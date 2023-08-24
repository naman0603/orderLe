package com.example.orderleapp.dataModel

import android.os.Parcel
import android.os.Parcelable

data class MyOrdersDataModel(
    val uri: String,
    val name: String,
    val type: String,
    val id: String,
    val price: String,
    val description : String,
    val orderId : String,val gold : String, val quantity:String,val weight : String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uri)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(id)
        parcel.writeString(price)
        parcel.writeString(description)
        parcel.writeString(orderId)
        parcel.writeString(gold)
        parcel.writeString(quantity)
        parcel.writeString(weight)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyOrdersDataModel> {
        override fun createFromParcel(parcel: Parcel): MyOrdersDataModel {
            return MyOrdersDataModel(parcel)
        }

        override fun newArray(size: Int): Array<MyOrdersDataModel?> {
            return arrayOfNulls(size)
        }
    }

}
