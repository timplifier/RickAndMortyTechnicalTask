package com.timplifier.rickandmortytechnicaltask.di

import android.content.Context
import android.content.SharedPreferences
import com.timplifier.common.constants.Constants.RICK_AND_MORTY_TECHNICAL_TASK_PREFERENCES
import com.timplifier.data.local.preferences.InternetConnectionPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun generateSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(
            RICK_AND_MORTY_TECHNICAL_TASK_PREFERENCES,
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun generateInternetConnectionPreferencesManager(sharedPreferences: SharedPreferences) =
        InternetConnectionPreferencesManager(sharedPreferences)
}