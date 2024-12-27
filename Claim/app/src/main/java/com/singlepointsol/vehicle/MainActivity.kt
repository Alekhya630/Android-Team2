package com.singlepointsol.vehicle

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.singlepointsol.vehicle.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val claimService = RetrofitInstance.getInstance().create(ClaimService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listeners
        binding.btnsave.setOnClickListener {
            fetchClaims()
        }

        binding.btnupdate.setOnClickListener {
            val updatedClaim = getClaimFromInput()
            val claimNo = binding.etClaimNo.text.toString()
            if (updatedClaim != null && claimNo.isNotEmpty()) {
                updateClaim(claimNo, updatedClaim)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btndelete.setOnClickListener {
            val claimNo = binding.etClaimNo.text.toString()
            if (claimNo.isNotEmpty()) {
                deleteClaim(claimNo)
            } else {
                Toast.makeText(this, "Please enter the ClaimNo to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getClaimFromInput(): ClaimItem? {
        val claimNo = binding.etClaimNo.text.toString()
        val claimDate = binding.etClaimDate.text.toString()
        val policyNo = binding.etPolicyNo.text.toString()
        val incidentDate = binding.etIncidentDate.text.toString()
        val incidentLocation = binding.etIncidentLocation.text.toString()
        val incidentDescription = binding.etIncidentDescription.text.toString()
        val claimAmount = binding.etClaimAmount.text.toString()
        val surveyorName = binding.etSurveyOrName.text.toString()
        val surveyorPhone = binding.etSurveyOrPhone.text.toString()
        val surveyDate = binding.etSurveyDate.text.toString()
        val surveyDescription = binding.etSurveyDescription.text.toString()
        val claimStatus = binding.etClaimStatus.text.toString()

        return if (claimNo.isNotEmpty() && claimDate.isNotEmpty() &&
            policyNo.isNotEmpty() && incidentDate.isNotEmpty() && incidentLocation.isNotEmpty() &&
            incidentDescription.isNotEmpty() && claimAmount.isNotEmpty() && surveyorName.isNotEmpty() &&
            surveyorPhone.isNotEmpty() && surveyDate.isNotEmpty() && surveyDescription.isNotEmpty() &&
            claimStatus.isNotEmpty()
        ) {
            ClaimItem(
                claimNo = claimNo,
                claimDate = claimDate,
                policyNo = policyNo,
                incidentDate = incidentDate,
                incidentLocation = incidentLocation,
                incidentDescription = incidentDescription,
                claimAmount = claimAmount,
                surveyorName = surveyorName,
                surveyorPhone = surveyorPhone,
                surveyDate = surveyDate,
                surveyDescription = surveyDescription,
                claimStatus = claimStatus
            )
        } else {
            null
        }
    }

    private fun fetchClaims() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = claimService.getClaims()
                if (response.isSuccessful) {
                    val claims = response.body()
                    if (claims != null && claims.isNotEmpty()) {
                        val claim = claims.first()
                        binding.etClaimNo.setText(claim.claimNo)
                        binding.etClaimDate.setText(claim.claimDate)
                        binding.etPolicyNo.setText(claim.policyNo)
                        binding.etIncidentDate.setText(claim.incidentDate)
                        binding.etIncidentDescription.setText(claim.incidentDescription)
                        binding.etIncidentLocation.setText(claim.incidentLocation)
                        binding.etSurveyOrName.setText(claim.surveyorName)
                        binding.etClaimAmount.setText(claim.claimAmount)
                        binding.etSurveyOrPhone.setText(claim.surveyorPhone)
                        binding.etSurveyDate.setText(claim.surveyDate)
                        binding.etSurveyDescription.setText(claim.surveyDescription)
                        binding.etClaimStatus.setText(claim.claimStatus)

                        Toast.makeText(this@MainActivity, "Fetched claims successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No claims available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch claims.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching claims: ${e.message}")
                Toast.makeText(this@MainActivity, "Error fetching claims.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun postClaim(claim: ClaimItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = claimService.postClaim(claim)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Claim added: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Claim added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to add claim.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to add claim.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error adding customer: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error adding claim.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateClaim(claimNo: String, claim: ClaimItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Use the claimService instance to call updateClaim method
                val response = claimService.updateClaim(claimNo, claim)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Claim updated: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Claim updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to update claim.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to update claim.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error updating claim: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error updating claim.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteClaim(claimNo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Use the claimService instance to call deleteClaim method
                val response = claimService.deleteClaim(claimNo)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Claim deleted.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Claim deleted successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to delete claim.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to delete claim.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting claim: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error deleting claim.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
