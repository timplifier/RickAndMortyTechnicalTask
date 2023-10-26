package com.timplifier.main.presentation.models

import com.timplifier.domain.models.LocationModel

data class LocationUI(
    val name: String,
)

fun LocationModel.toUI() = LocationUI(name)