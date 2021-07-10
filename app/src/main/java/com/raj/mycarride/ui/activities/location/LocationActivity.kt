package com.raj.mycarride.ui.activities.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.raj.mycarride.R
import kotlinx.android.synthetic.main.activity_demo_start.*
import kotlinx.android.synthetic.main.activity_location.*

class LocationActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var currentlocation: Location
    lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        initStuff()
        setListeners()

    }

    private fun initStuff() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5000)
        locationCallback = object : LocationCallback(){
            override fun onLocationAvailability(locationAvailable: LocationAvailability?) {
                super.onLocationAvailability(locationAvailable)
                if (locationAvailable!!.isLocationAvailable) {
                    Log.i("RAJKUMAR", "Location result is available")
                } else {
                    Log.i("RAJKUMAR", "Location result is unavailable")
                }

            }

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                Log.i("RAJKUMAR","Location result is available");

            }
        }



    }

    private fun setListeners() {
        buttonGetLocation.setOnClickListener {
            startGettingLocation()
        }

        buttonStopLocation.setOnClickListener {
            stopLocationRequests()//other wise battery will drain
        }
    }

    private fun stopLocationRequests() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startGettingLocation() {

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                Looper.getMainLooper())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
             currentlocation = it
                textViewLatitude.text = currentlocation.latitude.toString()
                textViewLongitude.text = currentlocation.longitude.toString()
            }

            fusedLocationProviderClient.lastLocation.addOnFailureListener(object:OnFailureListener{
                override fun onFailure(p0: Exception) {
                    Log.i("RAJKUMAR", p0.localizedMessage)
                }

            })

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this@LocationActivity, "Permission needed", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_PERMISSION)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        startGettingLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationRequests()
    }
}