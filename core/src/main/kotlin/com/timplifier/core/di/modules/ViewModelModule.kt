package com.timplifier.core.di.modules

import androidx.lifecycle.ViewModelProvider
import com.timplifier.core.utils.ViewModelFactory
import com.timplifier.data.di.modules.DataModule
import dagger.Binds
import dagger.Module

@Module(includes = [DataModule::class])
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}