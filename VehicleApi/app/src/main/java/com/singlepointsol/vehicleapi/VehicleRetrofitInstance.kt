package com.singlepointsol.vehicleapi

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleRetrofitInstance {

    companion object {
        private const val MAIN_URL = "https://abzvehiclewebapi-akshitha.azurewebsites.net/api/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}