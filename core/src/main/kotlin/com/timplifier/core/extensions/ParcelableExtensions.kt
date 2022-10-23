package com.timplifier.core.extensions

import android.os.Parcel
import com.timplifier.core.extensions.toByte

fun Parcel.write(value: Any?) {
    when (value) {
        is String -> writeString(value)
        is Int -> writeInt(value)
        is Long -> writeLong(value)
        is Double -> writeDouble(value)
        is Float -> writeFloat(value)
        is Boolean -> writeByte(value.toByte())
    }
}