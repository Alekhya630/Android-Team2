package com.singlepointsol.proposalapi

import com.google.gson.annotations.SerializedName

data class ProposalItem(

    @SerializedName("proposalNo") val proposalNo: String? = null,
    @SerializedName("regNo") val regNo: String? = null,
    @SerializedName("productID") val productID: String? = null,
    @SerializedName("customerID") val customerID: String? = null,
    @SerializedName("fromDate") val fromDate: String? = null,
    @SerializedName("toDate") val toDate: String? = null,
    @SerializedName("idv") val idv: String? = null,
    @SerializedName("agentID") val agentID: String? = null,
    @SerializedName("basicAmount") val basicAmount: String? = null,
    @SerializedName("totalAmount") val totalAmount: String? = null,

)
