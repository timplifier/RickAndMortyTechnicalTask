package com.timplifier.main.presentation.models

import com.timplifier.domain.models.OriginModel

data class OriginUI(
    val name: String,
    val url: String
)

fun OriginModel.toUI() = OriginUI(name, url)