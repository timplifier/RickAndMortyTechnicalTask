package com.timplifier.data.base


import com.google.gson.annotations.SerializedName
import com.timplifier.data.CharactersQuery

data class BaseInfo(
    @SerializedName("count")
    val count: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("next")
    val next: Int?,
    @SerializedName("prev")
    val prev: Int?
)

fun CharactersQuery.Info.toInfo() = BaseInfo(count, pages, next, prev)