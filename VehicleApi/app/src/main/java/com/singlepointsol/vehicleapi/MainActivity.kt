package com.singlepointsol.vehicleapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.singlepointsol.vehicleapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vehicleService = VehicleRetrofitInstance.getInstance().create(VehicleService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGet.setOnClickListener { fetchVehicles() }
        binding.btnAdd.setOnClickListener { handleAddVehicle() }
        binding.btnUpdate.setOnClickListener { handleUpdateVehicle() }
        binding.btnDelete.setOnClickListener { handleDeleteVehicle() }
    }

    private fun handleAddVehicle() {
        val newVehicle = getVehicleFromInput()
        if (newVehicle != null) {
            addVehicle(newVehicle)
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUpdateVehicle() {
        val updatedVehicle = getVehicleFromInput()
        val regNo = binding.regNoEt.text.toString()
        if (updatedVehicle != null && regNo.isNotEmpty()) {
            updateVehicle(regNo, updatedVehicle)
        } else {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDeleteVehicle() {
        val regNo = binding.regNoEt.text.toString()
        if (regNo.isNotEmpty()) {
            deleteVehicle(regNo)
        } else {
            Toast.makeText(this, "Please enter the Registration Number to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getVehicleFromInput(): VehiclesItem? {
        val regNo = binding.regNoEt.text.toString()
        val regAuthority = binding.regAuthorityEt.text.toString()
        val make = binding.makeEt.text.toString()
        val model = binding.modelEt.text.toString()
        val fuelType = binding.fuelTypeEt.text.toString()
        val variant = binding.variantEt.text.toString()
        val engineNo = binding.enginenoEt.text.toString()
        val chassisNo = binding.chassisnoEt.text.toString()
        val engineCapacity = binding.engineCapacityEt.text.toString()
        val seatingCapacity = binding.seatingCpacityEt.text.toString()
        val mfgYear = binding.mfgYearEt.text.toString()
        val regDate = binding.regdateEt.text.toString()
        val bodyType = binding.bodyTypeEt.text.toString()
        val leasedBy = binding.leasedbyEt.text.toString()
        val ownerId = binding.ownerIdEt.text.toString()

        return if (regNo.isNotEmpty() && make.isNotEmpty() && model.isNotEmpty()) {
            VehiclesItem(
                regNo, regAuthority, make, model, fuelType, variant,
                engineNo, chassisNo, engineCapacity, seatingCapacity,
                mfgYear, regDate, bodyType, leasedBy, ownerId
            )
        } else {
            null
        }
    }

    private fun fetchVehicles() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = vehicleService.getVehicle()
                if (response.isSuccessful) {
                    val vehicle = response.body()
                    if (vehicle != null && vehicle.isNotEmpty()) {
                        val vehicle = vehicle.first()
                        binding.regNoEt.setText(vehicle.regNo)
                        binding.regAuthorityEt.setText(vehicle.regAuthority)
                        binding.makeEt.setText(vehicle.make)
                        binding.modelEt.setText(vehicle.model)
                        binding.fuelTypeEt.setText(vehicle.fuelType)
                        binding.variantEt.setText(vehicle.variant)
                        binding.enginenoEt.setText(vehicle.engineNo)
                        binding.chassisnoEt.setText(vehicle.chassisNo)
                        binding.engineCapacityEt.setText(vehicle.engineCapacity)
                        binding.seatingCpacityEt.setText(vehicle.seatingCapacity)
                        binding.mfgYearEt.setText(vehicle.mfgYear)
                        binding.regdateEt.setText(vehicle.regDate)
                        binding.bodyTypeEt.setText(vehicle.bodyType)
                        binding.leasedbyEt.setText(vehicle.leasedBy)
                        binding.ownerIdEt.setText(vehicle.ownerId)
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

    private fun addVehicle(vehicle: VehiclesItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = vehicleService.postVehicle(vehicle)
                if (response.isSuccessful) {
                    showSuccess("Vehicle added successfully")
                } else {
                    showError("Failed to add vehicle")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun updateVehicle(regNo: String, vehicle: VehiclesItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = vehicleService.updateVehicle(regNo, vehicle)
                if (response.isSuccessful) {
                    showSuccess("Vehicle updated successfully")
                } else {
                    showError("Failed to update vehicle")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun deleteVehicle(regNo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = vehicleService.deleteVehicle(regNo)
                if (response.isSuccessful) {
                    showSuccess("Vehicle deleted successfully")
                } else {
                    showError("Failed to delete vehicle")
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