package com.raj.mycarride.di.modules

import com.raj.mycarride.ui.activities.MapViewActivity
import com.raj.mycarride.ui.activities.MapsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder  {
    @ContributesAndroidInjector
    abstract fun bindMainActivity() : MapsActivity

    @ContributesAndroidInjector
    abstract fun bindMapViewActivity() : MapViewActivity
}