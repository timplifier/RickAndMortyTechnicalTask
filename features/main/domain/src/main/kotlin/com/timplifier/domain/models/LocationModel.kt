package com.timplifier.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LocationModel(
    val name: String,
    val url: String
)