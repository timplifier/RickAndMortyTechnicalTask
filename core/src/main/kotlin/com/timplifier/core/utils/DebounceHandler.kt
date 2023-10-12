package com.timplifier.core.utils

import android.os.Handler
import android.os.Looper

object DebounceHandler {
    private val handler = Handler(Looper.myLooper()!!)

    fun post(func: () -> Unit) {
        handler.post(func)
    }

    fun postDelayed(delay: Long, func: () -> Unit) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(func, delay)
    }
}