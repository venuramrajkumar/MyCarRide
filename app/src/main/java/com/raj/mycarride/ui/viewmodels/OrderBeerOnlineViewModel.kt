package com.raj.mycarride.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.raj.mycarride.rest.repositories.OrderBeerRepo
import com.raj.mycarride.storage.BeerInfo
import javax.inject.Inject

class OrderBeerOnlineViewModel  @Inject constructor() : ViewModel(){
    @Inject
    lateinit var orderBeerRepo : OrderBeerRepo


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