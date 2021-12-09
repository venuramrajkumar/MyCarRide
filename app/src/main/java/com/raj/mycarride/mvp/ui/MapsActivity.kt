package com.raj.mycarride.mvp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.raj.mycarride.R
import com.raj.mycarride.mvp.data.NetworkService
import com.raj.mycarride.rest.utilretrofit.APIServiceProvider
import com.raj.mycarride.rest.utilretrofit.UserResponse
import com.raj.mycarride.ui.utils.MapUtils
import com.raj.mycarride.ui.utils.PermissionUtils
import com.raj.mycarride.ui.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_maps.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), MapsView, OnMapReadyCallback {

    companion object {
        private const val TAG = "MapsActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 999
        private const val PICKUP_REQUEST_CODE = 1
        private const val DROP_REQUEST_CODE = 2
    }

    private lateinit var presenter: MapsPresenter
    private lateinit var googleMap: GoogleMap
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private var currentLatLng: LatLng? = null
    private var pickUpLatLng: LatLng? = null
    private var dropLatLng: LatLng? = null
    private val nearbyCabMarkerList = arrayListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        ViewUtils.enableTransparentStatusBar(window,this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter = MapsPresenter(NetworkService())
        presenter.onAttach(this)
        setUpClickListener()

        //To Initiate Retrofit service call
        initRestCall()
    }

    private fun initRestCall() {
        var apiServiceProvider  = APIServiceProvider.getApiServiceProvider(
            "https://gorest.co.in/public/v1/users/",
        5000,5000,HttpLoggingInterceptor.Level.BODY)

        apiServiceProvider?.getUserData()?.enqueue( object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("MapsActivity", response.body().toString())
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("MapsActivity", t.localizedMessage.toString())
            }

        })
    }

    private fun setUpClickListener() {
        pickUpTextView.setOnClickListener {
            launchLocationAutoCompleteActivity(PICKUP_REQUEST_CODE)
        }
        dropTextView.setOnClickListener {
            launchLocationAutoCompleteActivity(DROP_REQUEST_CODE)
        }
    }

    private fun launchLocationAutoCompleteActivity(requestCode: Int) {
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(this)
        intent.putExtra("RequestCOde",requestCode)

        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object :
        ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult?) {
            handleOnActivityResult(result)
        }
    })

    private fun handleOnActivityResult(result: ActivityResult?) {
            // There are no request codes
            val data: Intent? = result?.data
            val requestCode = data?.getIntExtra("RequestCOde",0)
            if (requestCode == PICKUP_REQUEST_CODE || requestCode == DROP_REQUEST_CODE) {
                when (result?.resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        Log.d(TAG, "Place: " + place.name + ", " + place.id + ", " + place.latLng)
                        when (requestCode) {
                            PICKUP_REQUEST_CODE -> {
                                pickUpTextView.text = place.name
                                pickUpLatLng = place.latLng
                            }
                            DROP_REQUEST_CODE -> {
                                dropTextView.text = place.name
                                dropLatLng = place.latLng
                            }
                        }
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        val status: Status = Autocomplete.getStatusFromIntent(data!!)
                        Log.d(TAG, status.statusMessage!!)
                    }
                    RESULT_CANCELED -> {
                        Log.d(TAG, "Place Selection Canceled")
                    }
                }
            }


    }


    private fun moveCamera(latLng: LatLng?) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng!!))
    }

    private fun animateCamera(latLng: LatLng?) {
        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder().target(
                    latLng!!
                ).zoom(15.5f).build()
            )
        )
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f))
    }

    private fun addCarMarkerAndGet(latLng: LatLng): Marker {
        return googleMap.addMarker(
            MarkerOptions().position(latLng).flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(this)))
        )!!
    }

    private fun enableMyLocationOnMap() {
        googleMap.setPadding(0, ViewUtils.dpToPx(48f), 0, 0)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
                Toast.makeText(this,"No permission",Toast.LENGTH_SHORT).show()
            return
        }
        googleMap.isMyLocationEnabled = true
    }

    private fun setUpLocationListener() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (currentLatLng == null) {
                    for (location in locationResult.locations) {
                        if (currentLatLng == null) {
                            currentLatLng = LatLng(location.latitude, location.longitude)
                            enableMyLocationOnMap()
                            moveCamera(currentLatLng)
                            animateCamera(currentLatLng)
                            presenter.requestNearbyCabs(currentLatLng!!)
                        }
                    }
                }
                // Few more things we can do here:
                // For example: Update the location of user on server
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        if (currentLatLng == null) {
            when {
                PermissionUtils.isAccessFineLocationGranted(this) -> {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                }
                else -> {
                    PermissionUtils.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        presenter.onDetach()
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    override fun showNearbyCabs(latLngList: List<LatLng>) {
        nearbyCabMarkerList.clear()
        for (latLng in latLngList) {
            val nearbyCabMarker = addCarMarkerAndGet(latLng)
            nearbyCabMarkerList.add(nearbyCabMarker)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKUP_REQUEST_CODE || requestCode == DROP_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Log.d(TAG, "Place: " + place.name + ", " + place.id + ", " + place.latLng)
                    when (requestCode) {
                        PICKUP_REQUEST_CODE -> {
                            pickUpTextView.text = place.name
                            pickUpLatLng = place.latLng
                        }
                        DROP_REQUEST_CODE -> {
                            dropTextView.text = place.name
                            dropLatLng = place.latLng
                        }
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status: Status = Autocomplete.getStatusFromIntent(data!!)
                    Log.d(TAG, status.statusMessage!!)
                }
                Activity.RESULT_CANCELED -> {
                    Log.d(TAG, "Place Selection Canceled")
                }
            }
        }
    }

}
