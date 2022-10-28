package com.timplifier.data.local.preferences

import android.content.SharedPreferences
import com.timplifier.common.constants.Constants.SHOULD_AWARE_USER_ABOUT_LOST_INTERNET_CONNECTION_KEY
import javax.inject.Inject

class InternetConnectionPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    var shouldAwareUserAboutLostInternetConnection: Boolean
        get() = sharedPreferences.getBoolean(
            SHOULD_AWARE_USER_ABOUT_LOST_INTERNET_CONNECTION_KEY,
            true
        )
        set(value) = sharedPreferences.put(
            SHOULD_AWARE_USER_ABOUT_LOST_INTERNET_CONNECTION_KEY,
            value
        )
}