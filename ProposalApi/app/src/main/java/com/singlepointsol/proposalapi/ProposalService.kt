package com.singlepointsol.proposalapi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProposalService {

    @GET("Proposal")
    suspend fun getProposal(): Response<Proposal>

    @POST("Proposal")
    suspend fun postProposal(@Body vehicle: ProposalItem): Response<ProposalItem>

    @PUT("Proposal/{proposalNo}")
    suspend fun updateProposal(
        @Path("proposalNo") proposalNo: String,
        @Body vehicle: ProposalItem
    ): Response<ProposalItem>

    @DELETE("Proposal/{proposalNo}")
    suspend fun deleteProposal(@Path("proposalNo") proposalNo: String): Response<Unit>
}