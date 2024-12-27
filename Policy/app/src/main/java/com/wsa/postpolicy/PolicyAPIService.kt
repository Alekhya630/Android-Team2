package com.wsa.postpolicy

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PolicyAPIService {

@GET("Policy")
suspend fun getPolicy(): Response<PolicyArray>

@POST("Policy")
suspend fun postPolicy(@Body policy: PolicyItem): Response<PolicyItem>

    @PUT("Policy/{policyNo}")
    suspend fun updatePolicy(
        @Path("policyNo") policyNo: String,
        @Body policy: PolicyItem
    ): Response<PolicyItem>

    @DELETE("Policy/{policyNo}")
    suspend fun deletePolicy(@Path("policyNo") policyNo: String): Response<Unit>

}