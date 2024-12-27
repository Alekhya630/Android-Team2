package com.wsa.productretrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductAPIService {

    @GET("api/Product")
    suspend fun getProd(): Response<ProductArray>

    @POST("Product")
    suspend fun addProd(@Body product: ProductItem): Response<ProductItem>

    @PUT("Product/{productID}")
    suspend fun updateProd(@Path("productID") productID: String, @Body product: ProductItem): Response<ProductItem>

    @DELETE("Product/{productID}")
    suspend fun deleteProd(@Path("productID") productID: String): Response<Unit>

}