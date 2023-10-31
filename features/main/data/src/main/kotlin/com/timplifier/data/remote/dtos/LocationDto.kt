package com.timplifier.data.remote.dtos


import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.LocationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
) : DataMapper<LocationModel> {
    override fun toDomain() = LocationModel(name, url)
}

fun LocationModel.toData() = LocationDto(name, url)