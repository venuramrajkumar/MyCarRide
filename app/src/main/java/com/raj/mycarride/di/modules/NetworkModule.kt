package com.raj.mycarride.di.modules

import android.content.Context
import com.raj.mycarride.rest.IGetBeerInfoAPI
import com.raj.mycarride.storage.BeerDao
import com.raj.mycarride.storage.BeerDataBase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    fun provideImageDownloadAPI ( retrofit: Retrofit) : IGetBeerInfoAPI {
        return retrofit.create(IGetBeerInfoAPI::class.java)
    }

}