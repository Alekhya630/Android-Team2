package com.wsa.postpolicy

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wsa.postpolicy.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val policyAPIService = PolicyInstance.getInstance().create(PolicyAPIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paymentModeEditText: AutoCompleteTextView = findViewById(R.id.payment_mode_et)

        // Create an ArrayAdapter for the dropdown options
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_mode,  // The string-array resource containing options
            android.R.layout.simple_dropdown_item_1line // Dropdown item layout
        )


          // Set the adapter to the AutoCompleteTextView
        paymentModeEditText.setAdapter(adapter)



        // Set up button click listeners
        binding.btnGet.setOnClickListener {
            getallPolicy()
        }

        binding.btnPost.setOnClickListener {
            val newPolicy = getPolicyFromInput()
            if (newPolicy != null) {
                addPolicy(newPolicy)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPut.setOnClickListener {
            val updatepolicy = getPolicyFromInput()
            val policyNo = binding.etPolicyNo.text.toString()
            if (updatepolicy != null && policyNo.isNotEmpty()) {
                updatePolicy(policyNo, updatepolicy)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            val policyNo = binding.etPolicyNo.text.toString()
            if (policyNo.isNotEmpty()) {
                deletePolicy(policyNo)
            } else {
                Toast.makeText(this, "Please enter the Policy ID to delete", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getPolicyFromInput(): PolicyItem? {
        val policyNoEt = binding.etPolicyNo.text.toString()
        val proposalNoEt = binding.etProposalNo.text.toString()
        val noClaimBonusEt = binding.etNoClaimBonus.text.toString()
        val receiptNoEt = binding.etReceiptNo.text.toString()
        val receiptDateEt = binding.etReceiptDate.text.toString()
        val paymentet=binding.paymentModeEt.text.toString()
        val amountEt = binding.etAmount.text.toString()


        return if (policyNoEt.isNotEmpty() && proposalNoEt.isNotEmpty() &&
            noClaimBonusEt.isNotEmpty() && receiptNoEt.isNotEmpty() && receiptDateEt.isNotEmpty()
             && amountEt.isNotEmpty()
        ) {
            PolicyItem(
                policyNo = policyNoEt,
                proposalNo = proposalNoEt,
                noClaimBonus = noClaimBonusEt,
                receiptNo = receiptNoEt,
                receiptDate = receiptDateEt,
                paymentMode = paymentet,
                amount = amountEt

            )
        } else {
            null
        }
    }

    private fun getallPolicy() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = policyAPIService.getPolicy()
                if (response.isSuccessful) {
                    val policys = response.body()
                    if (policys!=null && policys.isNotEmpty()) {
                        val policy = policys.first()
                        binding.etPolicyNo.setText(policy.policyNo)
                        binding.etProposalNo.setText(policy.proposalNo)
                        binding.etNoClaimBonus.setText(policy.noClaimBonus)
                        binding.etReceiptNo.setText(policy.receiptNo)
                        binding.etReceiptDate.setText(policy.receiptDate)
                        binding.etAmount.setText(policy.amount)
                        binding.paymentModeEt.setText(policy.paymentMode)

                        Toast.makeText(this@MainActivity, "Fetched policy successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No policy available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch policy.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching policy: ${e.message}")
                Toast.makeText(this@MainActivity, "Error fetching policy.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPolicy(item: PolicyItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {


                // Make the API call to add the policy
                val response = policyAPIService.postPolicy(item)

                // Check if the response was successful
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Policy added: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Policy added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Log the error response code and the error body for debugging
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    Log.e("MainActivity", "Failed to add Policy. Response Code: $errorCode")
                    Log.e("MainActivity", "Response Body: $errorBody")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to add policy. $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Log the full exception details (including stack trace)
                Log.e("MainActivity", "Error adding customer: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error adding policy: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun updatePolicy(policyNo: String, policyItem: PolicyItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = policyAPIService.updatePolicy(policyNo, policyItem)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "policy updated: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "policy updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to update policy.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to update policy.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error updating policy: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error updating policy.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deletePolicy(policyNo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = policyAPIService.deletePolicy(policyNo)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Customer deleted.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "policy deleted successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to delete policy.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to delete policy.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting policy: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error deleting policy.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}