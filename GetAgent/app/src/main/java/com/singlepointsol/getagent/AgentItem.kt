package com.singlepointsol.getagent

import com.google.gson.annotations.SerializedName

data class AgentItem(
    @SerializedName("AgentID")var AgentID :String?=null,
    @SerializedName("AgentName") var AgentName : String?=null,
    @SerializedName("AgentPhone") var AgentPhone : String?=null,
    @SerializedName("AgentEmail") var AgentEmail : String?=null,
    @SerializedName("LicenseCode") var LicenseCode:String?=null

)
