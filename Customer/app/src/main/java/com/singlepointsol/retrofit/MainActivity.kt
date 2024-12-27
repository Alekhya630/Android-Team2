package com.singlepointsol.retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.singlepointsol.retrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val customerService = RetrofitInstance.getInstance().create(CustomerService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listeners
        binding.btnGet.setOnClickListener {
            fetchCustomers()
        }

        binding.btnAdd.setOnClickListener {
            val newCustomer = getCustomerFromInput()
            if (newCustomer != null) {
                addCustomer(newCustomer)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpdate.setOnClickListener {
            val updatedCustomer = getCustomerFromInput()
            val customerId = binding.customerId.text.toString()
            if (updatedCustomer != null && customerId.isNotEmpty()) {
                updateCustomer(customerId, updatedCustomer)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            val customerId = binding.customerId.text.toString()
            if (customerId.isNotEmpty()) {
                deleteCustomer(customerId)
            } else {
                Toast.makeText(this, "Please enter the Customer ID to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCustomerFromInput(): CustomerItem? {
        val customerId = binding.customerId.text.toString()
        val customerName = binding.customerName.text.toString()
        val customerPhone = binding.customerPhone.text.toString()
        val customerEmail = binding.customerEmail.text.toString()
        val customerAddress = binding.customerAddress.text.toString()

        return if (customerId.isNotEmpty() && customerName.isNotEmpty() &&
            customerPhone.isNotEmpty() && customerEmail.isNotEmpty() && customerAddress.isNotEmpty()
        ) {
            CustomerItem(
                customerID = customerId,
                customerName = customerName,
                customerPhone = customerPhone,
                customerEmail = customerEmail,
                customerAddress = customerAddress
            )
        } else {
            null
        }
    }

    private fun fetchCustomers() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = customerService.getCustomers()
                if (response.isSuccessful) {
                    val customers = response.body()
                    if (customers != null && customers.isNotEmpty()) {
                        val customer = customers.first()
                        binding.customerId.setText(customer.customerID)
                        binding.customerName.setText(customer.customerName)
                        binding.customerPhone.setText(customer.customerPhone)
                        binding.customerEmail.setText(customer.customerEmail)
                        binding.customerAddress.setText(customer.customerAddress)
                        Toast.makeText(this@MainActivity, "Fetched customers successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No customers available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch customers.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching customers: ${e.message}")
                Toast.makeText(this@MainActivity, "Error fetching customers.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCustomer(customer: CustomerItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = customerService.postCustomer(customer)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Customer added: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Customer added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to add customer.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to add customer.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error adding customer: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error adding customer.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateCustomer(customerID: String, customer: CustomerItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = customerService.updateCustomer(customerID, customer)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Customer updated: ${response.body()}")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Customer updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to update customer.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to update customer.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error updating customer: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error updating customer.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteCustomer(customerID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = customerService.deleteCustomer(customerID)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Customer deleted.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Customer deleted successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to delete customer.")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Failed to delete customer.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting customer: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error deleting customer.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
