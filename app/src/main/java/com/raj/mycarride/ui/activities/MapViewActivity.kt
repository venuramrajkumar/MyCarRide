package com.raj.mycarride.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.raj.mycarride.R
import com.raj.mycarride.di.modules.ViewModelFactory
import com.raj.mycarride.ui.viewmodels.MapsActivityViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_mapview.*
import java.util.*
import javax.inject.Inject

class MapViewActivity : DaggerAppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private  var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mapViewModel  by lazy {
        ViewModelProvider(this,viewModeFactory).get(MapsActivityViewModel::class.java)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var currentlocation: Location
    private lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION = 100

    @Inject
    lateinit var viewModeFactory: ViewModelFactory

    private lateinit var mapView : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapview)
        var bundle : Bundle? = null
        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle(resources.getString(R.string.google_maps_key))
        }
        mapView  = findViewById(R.id.mapView)
        mapView.onCreate(bundle)

        initStuff()
        startGettingLocation()

        Toast.makeText(this,mapViewModel.getString(),Toast.LENGTH_SHORT).show()

    }

    private fun initStuff() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
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
                Log.i("RAJKUMAR","Location result is available")
            }
        }

        buttonGetAddress.setOnClickListener {
            getAddress()
        }

    }

    private fun getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(this,"Geocoder not present",Toast.LENGTH_SHORT).show()
        } else {
            val geocoder : Geocoder = Geocoder(this, Locale.getDefault())
            var addressList : List<Address>? = null

            try {
                Log.i("RAJKUMAR 1", Thread.currentThread().id.toString())
               val disposable =  getGeoCoderLocation(geocoder)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                        {
                            addressList = it
                            updateUI(addressList)
                        },
                        {
                            textViewAddress.text = "Could not get address"
                            textViewAddress.setTextColor( resources.getColor(android.R.color.holo_red_dark,this.theme))
                        },
                        {
                            Toast.makeText(this,"OnComplete",Toast.LENGTH_SHORT).show()
                        }
                            )
                compositeDisposable.add(disposable)



            } catch (e:Exception) {
                textViewAddress.text = "Could not get address"
                textViewAddress.setTextColor( resources.getColor(android.R.color.holo_red_dark,this.theme))
            }


        }

    }

    private fun updateUI(addressList: List<Address>?) {
        if (addressList == null || addressList!!.isEmpty()) {
            textViewAddress.setTextColor(
                resources.getColor(
                    android.R.color.holo_red_dark,
                    this.theme
                )
            )
        } else {
            val addressBuilder = StringBuilder()
            val address = addressList!![0]
            for (i in 0..address.maxAddressLineIndex) {
                addressBuilder.append(
                    address.getAddressLine(i).trimIndent()
                )
            }
            val finalAddress = addressBuilder.toString()
            textViewAddress.text = finalAddress
            textViewAddress.setTextColor(
                resources.getColor(
                    android.R.color.holo_blue_bright,
                    this.theme
                )
            )
            Log.i("RAJKUMAR 3", Thread.currentThread().id.toString())
        }
    }

    private fun getGeoCoderLocation(geocoder: Geocoder) : Observable<List<Address>?> {
        return Observable.create { emitter ->
            try {
                Log.i("RAJKUMAR", Thread.currentThread().id.toString())
               emitter.onNext( geocoder.getFromLocation(currentlocation.latitude,currentlocation.longitude,1))
                emitter.onComplete()

        } catch (e:Exception) {
            emitter.onError(e)
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

                mapView.getMapAsync(this)
            }

            fusedLocationProviderClient.lastLocation.addOnFailureListener(object:
                OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Log.i("RAJKUMAR", p0.localizedMessage)
                }

            })

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this@MapViewActivity, "Permission needed", Toast.LENGTH_LONG).show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_PERMISSION)
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {

        var mapViewBundle = outState.getBundle(resources.getString(R.string.google_maps_key))
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(resources.getString(R.string.google_maps_key),mapViewBundle)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
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
        compositeDisposable.dispose()
        mapView.onDestroy()
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