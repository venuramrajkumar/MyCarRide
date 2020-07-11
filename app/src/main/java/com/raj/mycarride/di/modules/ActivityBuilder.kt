package com.raj.mycarride.di.modules

import com.raj.mycarride.MapsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder  {
    @ContributesAndroidInjector
    abstract fun bindMainActivity() : MapsActivity
}