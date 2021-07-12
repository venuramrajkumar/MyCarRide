package com.raj.mycarride.ui.activities.beershop

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.raj.mycarride.R
import com.raj.mycarride.databinding.ActivityOrderBeerBinding
import com.raj.mycarride.di.modules.ViewModelFactory
import com.raj.mycarride.storage.BeerInfo
import com.raj.mycarride.ui.databinding.OrderBeerOnlineBindingAdapter
import com.raj.mycarride.ui.viewmodels.OrderBeerOnlineViewModel

import javax.inject.Inject

class OrderBeerOnlineActivity : BaseActivity() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var beerOnlineViewModel: OrderBeerOnlineViewModel

    lateinit var beerInfoObserver: Observer<List<BeerInfo>>
    lateinit var binding: ActivityOrderBeerBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_beer)
        binding.viewModel = beerOnlineViewModel
        binding.lifecycleOwner = this
        initObserver()
        beerOnlineViewModel.initBeerInfo()
        beerOnlineViewModel.getBeerLiveData().observe(this,beerInfoObserver)
    }

    private fun initObserver() {
        showSpinner()
        beerInfoObserver = Observer {
            dismissSpinner()
            binding.viewModel?.beerInfo?.value = it
            binding.recyclerView.adapter?.notifyDataSetChanged()
            /*
            *   when (it!!.mStatus) {
                Resource.Status.SUCCESS -> {
                    dismissSpinner()
                    it.mStatus = Resource.Status.DONE
                }
                Resource.Status.LOADING -> {
                }

                Resource.Status.ERROR -> {
                }
                else -> {
                }
            }
        }
        beerliveData.removeObserver(beerInfoObserver)
        beerliveData.observe(this, beerInfoObserver)
            * */

        }

    }

    override fun getViewModel(): ViewModel {
        beerOnlineViewModel = ViewModelProviders.of(this, viewModelFactory).get(OrderBeerOnlineViewModel::class.java)
        return beerOnlineViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        beerOnlineViewModel.dispose()
    }

}