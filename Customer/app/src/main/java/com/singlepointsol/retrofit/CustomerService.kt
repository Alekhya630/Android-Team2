package com.singlepointsol.retrofit

import retrofit2.Response
import retrofit2.http.*

interface CustomerService {

    @GET("Customer")
    suspend fun getCustomers(): Response<Customers>

    @POST("Customer")
    suspend fun postCustomer(@Body customer: CustomerItem): Response<CustomerItem>

    @PUT("Customer/{id}")
    suspend fun updateCustomer(
        @Path("id") id: String,
        @Body customer: CustomerItem
    ): Response<CustomerItem>

    @DELETE("Customer/{id}")
    suspend fun deleteCustomer(@Path("id") id: String): Response<Unit>
}
