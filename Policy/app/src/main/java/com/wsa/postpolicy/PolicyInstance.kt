package com.wsa.postpolicy

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PolicyInstance {

    companion object {
        private const val mainURL = "https://abzpolicywebapi-akshitha.azurewebsites.net/api/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(mainURL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }

}