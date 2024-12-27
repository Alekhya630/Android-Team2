package com.singlepointsol.getagent

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AgentApiService {
    @GET("api/Agent")
    suspend fun getAgent(): retrofit2.Response<AgentArray>

}
