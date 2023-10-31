package com.timplifier.data.extensions

import io.ktor.util.StringValuesBuilder

internal fun StringValuesBuilder.append(name: String, value: String?) {
    value?.let {
        append(name, it)
    }
}

internal fun StringValuesBuilder.append(name: String, value: Any) {
    append(name, value.toString())
}