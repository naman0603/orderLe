package com.example.orderleapp.dataModel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData

class MyCartDataModel(
    val id: String,
    val imageUri: String,
    val name: String,
    val charges: String,
    val weight: String,
    var counter: String,
    val categoryName: String,
    val ringSize : String
) {
    val quantityLiveData = MutableLiveData<String>()
}