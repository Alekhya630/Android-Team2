package com.wsa.productretrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductInstance {

    companion object {
        private const val mainURL = "https://abzcustomerwebapi-chana.azurewebsites.net/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(mainURL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }

    }
}