package com.timplifier.data.base


import com.google.gson.annotations.SerializedName

data class BasePagingResponse<T>(
    @SerializedName("info")
    val info: BaseInfo,
    @SerializedName("results")
    val results: List<T>
)