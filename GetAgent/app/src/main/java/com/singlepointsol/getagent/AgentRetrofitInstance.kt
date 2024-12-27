package com.singlepointsol.getagent

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AgentRetrofitInstance {
    companion object{
        val mainURL = "https://abzagentwebapi-chana.azurewebsites.net/"
        fun getAgentRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(mainURL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}