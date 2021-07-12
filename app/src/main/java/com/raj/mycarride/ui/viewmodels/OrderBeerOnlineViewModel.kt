package com.raj.mycarride.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raj.mycarride.rest.repositories.OrderBeerRepo
import com.raj.mycarride.storage.BeerInfo
import javax.inject.Inject

class OrderBeerOnlineViewModel  @Inject constructor() : ViewModel(){
    @Inject
    lateinit var orderBeerRepo : OrderBeerRepo

    var beerInfo: MutableLiveData<List<BeerInfo>> = MutableLiveData(arrayListOf(BeerInfo("Abv","IBU",111,"RAM","Style",1.0f)))

    fun initBeerInfo() {
        orderBeerRepo.getBeerInfoApi()
//        orderBeerRepo.demoMapOperator()
//        orderBeerRepo.demoFlatMapOperator()
//        orderBeerRepo.demoZipOperator()

        //Not working
//            orderBeerRepo.demoFlowableBackPressure()

    }

    fun getBeerLiveData() : LiveData<List<BeerInfo>> { //Resource<>
        return orderBeerRepo.getBeerLiveData()
    }

    fun dispose() {
        orderBeerRepo.disposeElements()
    }

}