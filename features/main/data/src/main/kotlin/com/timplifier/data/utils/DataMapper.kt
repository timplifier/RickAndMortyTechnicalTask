package com.timplifier.data.utils

interface DataMapper<T> {
    fun toDomain(): T
}