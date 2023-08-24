package com.example.orderleapp.dataModel

import android.os.Parcel
import android.os.Parcelable

data class ProductViewDataModel(val imageUri :String,
                                val name :String,
                                val category :String,
                                val description : String,
                                val charges :String,
                                val weight : String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUri)
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeString(charges)
        parcel.writeString(weight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductViewDataModel> {
        override fun createFromParcel(parcel: Parcel): ProductViewDataModel {
            return ProductViewDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductViewDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
