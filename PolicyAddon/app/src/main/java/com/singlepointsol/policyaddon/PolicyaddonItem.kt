package com.singlepointsol.policyaddon

import com.google.gson.annotations.SerializedName

data class PolicyaddonItem(
    @SerializedName("addonID") val addonID: String? = null,
    @SerializedName("policyNo") val policyNo: String? = null,
    @SerializedName("amount") val amount: String? = null,

)
