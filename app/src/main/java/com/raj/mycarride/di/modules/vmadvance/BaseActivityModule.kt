package com.raj.mycarride.di.modules.vmadvance

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module

@Module
abstract class BaseActivityModule<A : AppCompatActivity> {

    @Binds
    abstract fun provideActivity(activity: A): AppCompatActivity
}
