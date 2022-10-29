package com.timplifier.core.extensions

import android.util.Log

fun loge(tag: String = "gaypop", msg: String, value: Any? = null) = Log.e(tag, msg + value)