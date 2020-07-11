package com.raj.mycarride.di.modules

import android.content.Context
import com.raj.mycarride.MyCarRideApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun getAppContext(app:MyCarRideApp): Context {
        return app
    }
}