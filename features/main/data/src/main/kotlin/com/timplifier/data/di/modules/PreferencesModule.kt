package com.timplifier.data.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.timplifier.common.constants.Constants.RICK_AND_MORTY_TECHNICAL_TASK_PREFERENCES
import com.timplifier.data.local.preferences.InternetConnectionPreferencesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PreferencesModule {

    @Singleton
    @Provides
    fun generateSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            RICK_AND_MORTY_TECHNICAL_TASK_PREFERENCES,
            Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun generateInternetConnectionPreferencesManager(sharedPreferences: SharedPreferences) =
        InternetConnectionPreferencesManager(sharedPreferences)
}