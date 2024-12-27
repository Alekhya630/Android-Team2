package com.singlepointsol.retrofit

import com.google.gson.annotations.SerializedName

data class CustomerItem(
    @SerializedName("customerID") val customerID: String? = null,
    @SerializedName("customerName") val customerName: String? = null,
    @SerializedName("customerPhone") val customerPhone: String? = null,
    @SerializedName("customerEmail") val customerEmail: String? = null,
    @SerializedName("customerAddress") val customerAddress: String? = null
)
