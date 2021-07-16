package com.raj.mycarride.ui.viewmodels

import android.view.View
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
    var titleLiveData : MutableLiveData<String> = MutableLiveData("Title")
    var buttonText : MutableLiveData<String> = MutableLiveData("Click Me")
    var titleVisibility : MutableLiveData<Int> = MutableLiveData(0)



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

    fun setButtonText() {
        titleLiveData.value = "Changed"
    }

    fun toggleVisibility() {
        //view.visibility = if (show) View.VISIBLE else View.GONE
        if (titleVisibility.value == View.VISIBLE)
            titleVisibility.value = View.GONE
        else
            titleVisibility.value = View.VISIBLE
    }

    fun dispose() {
        orderBeerRepo.disposeElements()
    }

}