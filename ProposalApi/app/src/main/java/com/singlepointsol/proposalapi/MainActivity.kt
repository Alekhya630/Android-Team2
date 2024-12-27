package com.singlepointsol.proposalapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.singlepointsol.proposalapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val proposalService = ProposalRetrofitInstance.getInstance().create(ProposalService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGet.setOnClickListener { fetchProposal() }
        binding.btnAdd.setOnClickListener { handleAddProposal() }
        binding.btnUpdate.setOnClickListener { handleUpdateProposal() }
        binding.btnDelete.setOnClickListener { handleDeleteProposal() }
    }

    private fun handleAddProposal() {
        val newProposal = getProposalFromInput()
        if (newProposal != null) {
            addProposal(newProposal)
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUpdateProposal() {
        val updatedProposal = getProposalFromInput()
        val proposalNo = binding.proposalNoEt.text.toString()
        if (updatedProposal != null && proposalNo.isNotEmpty()) {
            updateProposal(proposalNo, updatedProposal)
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDeleteProposal() {
        val proposalNo = binding.proposalNoEt.text.toString()
        if (proposalNo.isNotEmpty()) {
            deleteProposal(proposalNo)
        } else {
            Toast.makeText(this, "Please enter the Proposal Number to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getProposalFromInput(): ProposalItem? {
        val proposalNo = binding.proposalNoEt.text.toString()
        val regNo = binding.regNoEt.text.toString()
        val productId = binding.productIdEt.text.toString()
        val customerId = binding.customerIdEt.text.toString()
        val fromDate = binding.fromDateEt.text.toString()
        val toDate = binding.toDateEt.text.toString()
        val idv = binding.idvEt.text.toString()
        val agentId = binding.agentIdEt.text.toString()
        val basicAmount = binding.basicAmountEt.text.toString()
        val totalAmount = binding.totalAmountEt.text.toString()

        return if (proposalNo.isNotEmpty() && regNo.isNotEmpty() && productId.isNotEmpty()) {
            ProposalItem(
                proposalNo = proposalNo,
                regNo = regNo,
                productID = productId,
                customerID = customerId,
                fromDate = fromDate,
                toDate = toDate,
                idv = idv,
                agentID = agentId,
                basicAmount = basicAmount,
                totalAmount = totalAmount
            )
        } else {
            null
        }
    }

    private fun fetchProposal() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = proposalService.getProposal()
                if (response.isSuccessful) {
                    val proposals = response.body()
                    if (proposals != null && proposals.isNotEmpty()) {
                        val proposal = proposals.first()
                        binding.proposalNoEt.setText(proposal.proposalNo)
                        binding.regNoEt.setText(proposal.regNo)
                        binding.productIdEt.setText(proposal.productID)
                        binding.customerIdEt.setText(proposal.customerID)
                        binding.fromDateEt.setText(proposal.fromDate)
                        binding.toDateEt.setText(proposal.toDate)
                        binding.idvEt.setText(proposal.idv)
                        binding.agentIdEt.setText(proposal.agentID)
                        binding.basicAmountEt.setText(proposal.basicAmount)
                        binding.totalAmountEt.setText(proposal.totalAmount)
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

    private fun addProposal(proposal: ProposalItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = proposalService.postProposal(proposal)
                if (response.isSuccessful) {
                    showSuccess("Proposal added successfully")
                } else {
                    showError("Failed to add proposal")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun updateProposal(proposalNo: String, proposal: ProposalItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = proposalService.updateProposal(proposalNo, proposal)
                if (response.isSuccessful) {
                    showSuccess("Proposal updated successfully")
                } else {
                    showError("Failed to update proposal")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun deleteProposal(proposalNo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = proposalService.deleteProposal(proposalNo)
                if (response.isSuccessful) {
                    showSuccess("Proposal deleted successfully")
                } else {
                    showError("Failed to delete proposal")
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
