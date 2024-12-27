package com.singlepointsol.retrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.singlepointsol.getagent.AgentApiService
import com.singlepointsol.getagent.AgentArray
import com.singlepointsol.getagent.AgentRetrofitInstance
import com.singlepointsol.getagent.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Create the Retrofit instance and API service
        val retrofitService =
            AgentRetrofitInstance.getAgentRetrofitInstance().create(AgentApiService::class.java)

        // Use LiveData to fetch data from the API
        val responseLiveData: LiveData<Response<AgentArray>> = liveData {
            try {
                val response = retrofitService.getAgent()
                emit(response)
            } catch (e: Exception) {
                Log.e("MainActivity", "API Call Failed: ${e.message}")
            }
        }

        // Observe the LiveData
        responseLiveData.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val agentList = response.body()

                if (agentList != null && agentList.isNotEmpty()) {
                    val stringBuilder = StringBuilder()

                    // Iterate through the list using forEach and append agent details
                    agentList.forEach { agent ->
                        val AgentId = agent.AgentID ?: ""
                        val AgentName = agent.AgentName ?: ""
                        val AgentPhone = agent.AgentPhone ?: ""
                        val AgentEmail = agent.AgentEmail ?: ""
                        val LicenseCode = agent.LicenseCode ?: ""

                        // Add agent details to stringBuilder
                        stringBuilder.append(
                            "ID: $AgentId\n" +
                                    "Name: $AgentName\n" +
                                    "Phone: $AgentPhone\n" +
                                    "Email: $AgentEmail\n" +
                                    "License: $LicenseCode\n\n"
                        )
                    }
                    // Finally, update the UI with the accumulated data
                    binding.agentIdTv.text = stringBuilder.toString()
                } else {
                    Log.e("MainActivity", "No agents found.")
                    binding.agentIdTv.text = "No agents available."
                }
            } else {
                Log.e(
                    "MainActivity",
                    "API Response Error: ${response.code()} - ${response.message()}"
                )
                binding.agentIdTv.text = "Failed to load data from API."
            }
        })

    }
}
