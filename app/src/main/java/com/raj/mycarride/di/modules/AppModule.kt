package com.raj.mycarride.di.modules

import android.content.Context
import com.raj.mycarride.storage.BeerDao
import com.raj.mycarride.storage.BeerDataBase
import com.raj.mycarride.MyCarRideApp
import com.raj.mycarride.di.builders.ActivityBindingModule
import com.raj.mycarride.di.qualifier.AppContext
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjection
import javax.inject.Singleton

@Module
class AppModule {

//    @Provides
//    @Singleton
//    fun getAppContext(app: MyCarRideApp) : Context {
//        return app
//    }

    @Singleton
    @Provides
    fun provideDatabase(appContext: Context): BeerDataBase = BeerDataBase.getInstance(appContext)

    @Singleton
    @Provides
    fun provideBeerDao(beerDataBase: BeerDataBase): BeerDao = beerDataBase.getBeerDao()

}