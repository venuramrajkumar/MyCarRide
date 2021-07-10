package com.raj.mycarride.ui.activities.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.raj.mycarride.R
import com.raj.mycarride.di.modules.ViewModelFactory
import com.raj.mycarride.ui.viewmodels.MapsActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_location.*
import javax.inject.Inject

class MapsActivity : DaggerAppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    val mapViewModel  by lazy {
        ViewModelProvider(this,viewModeFactory).get(MapsActivityViewModel::class.java)
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var currentlocation: Location
    lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION = 100

    @Inject
    lateinit var viewModeFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        initStuff()
        startGettingLocation()

        Toast.makeText(this,mapViewModel.getString(),Toast.LENGTH_SHORT).show()



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

    private fun stopLocationRequests() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startGettingLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
                Looper.getMainLooper())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                currentlocation = it
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }

            fusedLocationProviderClient.lastLocation.addOnFailureListener(object:
                OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Log.i("RAJKUMAR", p0.localizedMessage)
                }

            })

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this@MapsActivity, "Permission needed", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_PERMISSION)
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val myLocation = LatLng(currentlocation.latitude,currentlocation.longitude)
        mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in India"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15.0f))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

    }
}