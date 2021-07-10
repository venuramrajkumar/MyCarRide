package com.raj.mycarride.di.builders



import com.raj.mycarride.di.modules.NetworkModule
import com.raj.mycarride.di.modules.RetrofitModule
import com.raj.mycarride.di.modules.ViewModelModule
import com.raj.mycarride.di.qualifier.ActivityScope
import com.raj.mycarride.ui.activities.beershop.OrderBeerOnlineActivity
import com.raj.mycarride.ui.activities.location.MapViewActivity
import com.raj.mycarride.ui.activities.location.MapsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module (includes = [RetrofitModule::class, NetworkModule::class,ViewModelModule::class])
abstract class ActivityBindingModule {


    @ContributesAndroidInjector //(modules = [OrderBeerOnlineActivityModule::class])
    abstract fun bindOrderBeerOnlineActivity(): OrderBeerOnlineActivity

    @ContributesAndroidInjector
    abstract fun bindMainActivity() : MapsActivity

    @ContributesAndroidInjector
    abstract fun bindMapViewActivity() : MapViewActivity
}