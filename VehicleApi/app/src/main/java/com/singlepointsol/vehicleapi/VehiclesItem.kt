package com.singlepointsol.vehicleapi

import com.google.gson.annotations.SerializedName

data class VehiclesItem(
    @SerializedName("regNo") val regNo: String? = null,
    @SerializedName("regAuthority") val regAuthority: String? = null,
    @SerializedName("make") val make: String? = null,
    @SerializedName("model") val model: String? = null,
    @SerializedName("fuelType") val fuelType: String? = null,
    @SerializedName("variant") val variant: String? = null,
    @SerializedName("engineNo") val engineNo: String? = null,
    @SerializedName("chassisNo") val chassisNo: String? = null,
    @SerializedName("engineCapacity") val engineCapacity: String? = null,
    @SerializedName("seatingCapacity") val seatingCapacity: String? = null,
    @SerializedName("mfgYear") val mfgYear: String? = null,
    @SerializedName("regDate") val regDate: String? = null,
    @SerializedName("bodyType") val bodyType: String? = null,
    @SerializedName("leasedBy") val leasedBy: String? = null,
    @SerializedName("ownerId") val ownerId: String? = null
)
