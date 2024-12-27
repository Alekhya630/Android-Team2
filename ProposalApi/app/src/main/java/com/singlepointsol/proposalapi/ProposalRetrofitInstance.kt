package com.singlepointsol.proposalapi

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProposalRetrofitInstance {

    companion object {
        private const val MAIN_URL = "https://abzproposalwebapi-akshitha.azurewebsites.net/api/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}