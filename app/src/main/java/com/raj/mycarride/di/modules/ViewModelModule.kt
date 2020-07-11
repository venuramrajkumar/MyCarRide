package com.raj.mycarride.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.raj.mycarride.di.qualifier.ViewModelKey
import com.raj.mycarride.ui.viewmodels.MapsActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MapsActivityViewModel::class)
    abstract fun bindViewModel(viewModel: MapsActivityViewModel) : ViewModel

}