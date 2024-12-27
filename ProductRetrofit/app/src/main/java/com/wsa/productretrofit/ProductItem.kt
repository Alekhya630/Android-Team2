package com.wsa.productretrofit

import com.google.gson.annotations.SerializedName

data class ProductItem(

    @SerializedName("productID") val productID: String? = null,
    @SerializedName("productName") val productName: String? = null,
    @SerializedName("productDescription") val productDescription: String? = null,
    @SerializedName("productUIN") val productUIN: String? = null,
    @SerializedName("insuredInterests") val insuredInterests: String? = null,
    @SerializedName("policyCoverage") val policyCoverage: String? = null

    )
