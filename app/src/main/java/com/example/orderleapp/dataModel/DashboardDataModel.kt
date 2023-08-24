package com.example.orderleapp.dataModel

import android.os.Parcel
import android.os.Parcelable

data class DashboardDataModel( val imageUri: String,  val name: String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUri)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DashboardDataModel> {
        override fun createFromParcel(parcel: Parcel): DashboardDataModel {
            return DashboardDataModel(parcel)
        }

        override fun newArray(size: Int): Array<DashboardDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
