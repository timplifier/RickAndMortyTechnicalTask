package com.timplifier.data.di.modules

import dagger.Module

@Module(includes = [LocalModule::class, PreferencesModule::class, RepositoryModule::class])
object DataModule