package com.singlepointsol.policyaddon

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.singlepointsol.policyaddon.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val policyaddonService = PolicyaddonRetrofitInstance.getInstance().create(PolicyaddonService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGet.setOnClickListener { fetchPolicyaddon() }
        binding.btnAdd.setOnClickListener { handleAddPolicyaddon() }
        binding.btnUpdate.setOnClickListener { handleUpdatePolicyaddon() }
        binding.btnDelete.setOnClickListener { handleDeletePolicyaddon() }
    }

    private fun handleAddPolicyaddon() {
        val newPolicyaddon = getPolicyaddonFromInput()
        if (newPolicyaddon != null) {
            addPolicyaddon(newPolicyaddon)
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUpdatePolicyaddon() {
        val updatedPolicyaddon = getPolicyaddonFromInput()
        val policyNo = binding.policyNoEt.text.toString()
        val addonID = binding.addonIdEt.text.toString()
        if (updatedPolicyaddon != null && policyNo.isNotEmpty() && addonID.isNotEmpty()) {
            updatePolicyaddon(policyNo, addonID, updatedPolicyaddon)
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDeletePolicyaddon() {
        val policyNo = binding.policyNoEt.text.toString()
        val addonID = binding.addonIdEt.text.toString()
        if (policyNo.isNotEmpty() && addonID.isNotEmpty()) {
            deletePolicyaddon(policyNo, addonID)
        } else {
            Toast.makeText(this, "Please enter the policy number and addon ID to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPolicyaddonFromInput(): PolicyaddonItem? {
        val policyNo = binding.policyNoEt.text.toString()
        val addonID = binding.addonIdEt.text.toString()
        val amount = binding.amountEt.text.toString()

        return if (policyNo.isNotEmpty() && addonID.isNotEmpty() && amount.isNotEmpty()) {
            PolicyaddonItem(
                policyNo = policyNo,
                addonID = addonID,
                amount = amount
            )
        } else {
            null
        }
    }

    private fun fetchPolicyaddon() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = policyaddonService.getPolicyaddon()
                if (response.isSuccessful) {
                    val policyaddon = response.body()
                    if (policyaddon != null && policyaddon.isNotEmpty()) {
                        val firstPolicyaddon = policyaddon.first()
                        binding.policyNoEt.setText(firstPolicyaddon.policyNo)
                        binding.addonIdEt.setText(firstPolicyaddon.addonID)
                        binding.amountEt.setText(firstPolicyaddon.amount)

                        Toast.makeText(this@MainActivity, "Fetched proposals successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No proposals available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch proposals.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching proposals: ${e.message}")
                Toast.makeText(this@MainActivity, "Error fetching proposals.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPolicyaddon(policy: PolicyaddonItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = policyaddonService.postPolicyaddon(policy)
                if (response.isSuccessful) {
                    showSuccess("Policy added successfully")
                } else {
                    showError("Failed to add policy")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun updatePolicyaddon(policyNo: String, addonID: String, policy: PolicyaddonItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = policyaddonService.updatePolicyaddon(policyNo, addonID, policy)
                if (response.isSuccessful) {
                    showSuccess("Policy updated successfully")
                } else {
                    showError("Failed to update policy: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }



    private fun deletePolicyaddon(policyNo: String, addonID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = policyaddonService.deletePolicyaddon(policyNo, addonID)
                if (response.isSuccessful) {
                    showSuccess("Policy deleted successfully")
                } else {
                    showError("Failed to delete policy: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }


    private fun showSuccess(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
