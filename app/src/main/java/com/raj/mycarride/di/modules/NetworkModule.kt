//package com.example.mvvmsimplified.di.modules
//
//import com.example.mvvmsimplified.rest.IGetBeerInfoAPI
//import dagger.Module
//import dagger.Provides
//import retrofit2.Retrofit
//
//@Module
//class NetworkModule {
//
//    @Provides
//    fun provideImageDownloadAPI ( retrofit: Retrofit) : IGetBeerInfoAPI {
//        return retrofit.create(IGetBeerInfoAPI::class.java)
//    }
//
//}