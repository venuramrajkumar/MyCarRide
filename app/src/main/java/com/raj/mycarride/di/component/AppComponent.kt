package com.raj.mycarride.di.component

import android.content.Context
import com.raj.mycarride.MyCarRideApp
import com.raj.mycarride.di.builders.ActivityBindingModule
import com.raj.mycarride.di.modules.AppModule
import com.raj.mycarride.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component (modules = [AppModule::class,AndroidSupportInjectionModule::class,ViewModelModule::class,ActivityBindingModule::class ])
interface AppComponent : AndroidInjector<MyCarRideApp>{

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}