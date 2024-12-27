package com.singlepointsol.policyaddon

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PolicyaddonService {

    @GET("PolicyAddon")
    suspend fun getPolicyaddon(): Response<Policyaddon>

    @POST("PolicyAddon")
    suspend fun postPolicyaddon(@Body policy: PolicyaddonItem): Response<PolicyaddonItem>

    @PUT("PolicyAddon/{policyNo}/{addonID}")
    suspend fun updatePolicyaddon(
        @Path("policyNo") policyNo: String,
        @Path("addonID") addonID: String,
        @Body policy: PolicyaddonItem
    ): Response<PolicyaddonItem>

    @DELETE("PolicyAddon/{policyNo}/{addonID}")
    suspend fun deletePolicyaddon(
        @Path("policyNo") policyNo: String,
        @Path("addonID") addonID: String
    ): Response<Unit>
}
