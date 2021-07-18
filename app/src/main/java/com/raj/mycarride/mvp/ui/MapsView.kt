package com.raj.mycarride.mvp.ui

import com.google.android.gms.maps.model.LatLng

interface MapsView {

    fun showNearbyCabs(latLngList: List<LatLng>)

}