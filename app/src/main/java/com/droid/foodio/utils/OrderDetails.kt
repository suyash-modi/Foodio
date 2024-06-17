package com.droid.foodio.utils

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OrderDetails():Serializable {

    var userUid : String? = null
    var userName : String? = null
    var foodNames : MutableList<String>? = null
    var foodImages : MutableList<String>? = null
    var foodPrices : MutableList<String>? = null
    var foodQuantities : MutableList<Int>? = null
    var address : String? = null
    var totalPrice : String? = null
    var phoneNumber : String? = null
    var orderAccepted : Boolean= false
    var paymentReceived : Boolean= false
    var itemPushKey : String? = null
    var currentTime : Long= 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemQuantity: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phoneNo: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ) : this()
    {
        this.userUid = userId
        this.userName=name
        this.foodNames = foodItemName
        this.foodImages = foodItemImage
        this.foodPrices = foodItemPrice
        this.foodQuantities = foodItemQuantity
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phoneNo
        this.orderAccepted = b
        this.paymentReceived = b1
        this.itemPushKey = itemPushKey
        this.currentTime = time


    }

     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

     fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}