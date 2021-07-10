package com.raj.mycarride.rest

import com.raj.mycarride.storage.BeerInfo
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET

interface IGetBeerInfoAPI {

    @GET("beercraft")
    fun getBeerInfo() : Observable<List<BeerInfo>>

    @GET("beercraft")
    fun getBeerInfoFlowable() : Flowable<List<BeerInfo>>

}