package com.timplifier.core.extensions

fun Boolean.toByte() = (if (this) 1 else 0).toByte()