package com.singlepointsol.vehicleapi

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VehicleService {

    @GET("Vehicle")
    suspend fun getVehicle(): Response<Vehicles>

    @POST("Vehicle")
    suspend fun postVehicle(@Body vehicle: VehiclesItem): Response<VehiclesItem>

    @PUT("Vehicle/{regNo}")
    suspend fun updateVehicle(
        @Path("regNo") regNo: String,
        @Body vehicle: VehiclesItem
    ): Response<VehiclesItem>

    @DELETE("Vehicle/{regNo}")
    suspend fun deleteVehicle(@Path("regNo") regNo: String): Response<Unit>
}