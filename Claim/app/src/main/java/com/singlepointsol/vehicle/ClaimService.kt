package com.singlepointsol.vehicle

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClaimService {
    @GET("Claim")
    suspend fun getClaims(): Response<Claims>


    @POST("Claim")
    suspend fun postClaim(@Body claim: ClaimItem): Response<ClaimItem>

    @PUT("Claim/{No}")
    suspend fun updateClaim(
        @Path("No") id: String,
        @Body claim: ClaimItem
    ): Response<ClaimItem>

    @DELETE("claims/{claimNo}")
    suspend fun deleteClaim(@Path("claimNo") claimNo: String): Response<Void>
}