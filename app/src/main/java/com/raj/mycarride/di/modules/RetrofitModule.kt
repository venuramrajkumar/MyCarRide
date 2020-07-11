package com.raj.mycarride.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {

    val baseImageUrl: String
        get() = "http://starlord.hackerearth.com/"


    @Provides
    fun provideRetrofit (gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient, rxJava2CallAdapterFactory: RxJava2CallAdapterFactory) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseImageUrl)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(gson: Gson) :GsonConverterFactory{
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideGson(gsonBuilder: GsonBuilder) : Gson{
        return gsonBuilder.create()
    }

    @Provides
    fun provideGsonBuilder() :GsonBuilder{
        return GsonBuilder()
    }

    @Provides
    fun provideHttpLoginInterceptor () : HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =  HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    fun provideokHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000,TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideRxAdapter () : RxJava2CallAdapterFactory  {
        return RxJava2CallAdapterFactory.create()
    }

}