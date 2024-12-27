package com.wsa.productretrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wsa.productretrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val productAPIService =
        ProductInstance.getInstance().create(ProductAPIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listeners
        binding.getBtn.setOnClickListener { fetchProduct() }

        binding.postBtn.setOnClickListener {
            val newProduct = getProductFromInput()
            if (newProduct != null) {
                postProduct(newProduct)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.putBtn.setOnClickListener {
            val updateProduct = getProductFromInput()
            val productID = binding.productIdEt.text.toString()
            if (updateProduct != null && productID.isNotEmpty()) {
                updateProductInActivity(productID, updateProduct)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteBtn.setOnClickListener {
            val productID = binding.productIdEt.text.toString()
            if (productID.isNotEmpty()) {
                deleteProduct(productID)
            } else {
                Toast.makeText(this, "Please enter a Product ID to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getProductFromInput(): ProductItem? {
        val productID = binding.productIdEt.text.toString()
        val productName = binding.prodnameEt.text.toString()
        val productDescription = binding.proddescEt.text.toString()
        val productUIN = binding.produinEt.text.toString()
        val insuredInterests = binding.insuredEt.text.toString()
        val policyCoverage = binding.policycoverageEt.text.toString()

        return if (productID.isNotEmpty() && productName.isNotEmpty() && productDescription.isNotEmpty()
            && productUIN.isNotEmpty() && insuredInterests.isNotEmpty() && policyCoverage.isNotEmpty()) {
            ProductItem(
                productID = productID,
                productName = productName,
                productDescription = productDescription,
                productUIN = productUIN,
                insuredInterests = insuredInterests,
                policyCoverage = policyCoverage,
            )
        } else {
            null
        }
    }

    private fun fetchProduct() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = productAPIService.getProd()
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null && product.isNotEmpty()) {
                        val Products = product.first()
                        // Set the values to EditTexts
                        binding.productIdEt.setText(Products.productID)
                        binding.prodnameEt.setText(Products.productName)
                        binding.proddescEt.setText(Products.productDescription)
                        binding.produinEt.setText(Products.productUIN)
                        binding.insuredEt.setText(Products.insuredInterests)
                        binding.policycoverageEt.setText(Products.policyCoverage)

                        Toast.makeText(this@MainActivity, "Fetched product successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No Product found.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch product.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching product: ${e.message}")
                Toast.makeText(this@MainActivity, "Error fetching product.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postProduct(Item: ProductItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = productAPIService.addProd(Item)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Product added: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Product added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to add product.")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to add Product.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error adding product: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error adding Product.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateProductInActivity(productID: String,productItem: ProductItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = productAPIService.updateProd(productID, productItem)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Product updated: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to update product.")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to update Product.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error updating product: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error updating Product.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteProduct(productID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = productAPIService.deleteProd(productID)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Product deleted: $productID")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Product deleted successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Failed to delete product.")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to delete Product.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error deleting product: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error deleting Product.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
