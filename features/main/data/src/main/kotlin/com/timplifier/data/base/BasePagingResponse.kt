package com.timplifier.data.base


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasePagingResponse<T>(
    @SerialName("info")
    val info: BaseInfo,
    @SerialName("results")
    val results: List<T>
)