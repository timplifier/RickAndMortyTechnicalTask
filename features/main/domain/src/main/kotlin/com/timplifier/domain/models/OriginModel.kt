package com.timplifier.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class OriginModel(
    val name: String,
    val url: String
)