package com.timplifier.data.remote.dtos


import com.google.gson.annotations.SerializedName
import com.timplifier.data.fragment.CharacterFragment
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.LocationModel

data class LocationDto(
    @SerializedName("name")
    val name: String,
) : DataMapper<LocationModel> {
    override fun toDomain() = LocationModel(name)
}

fun LocationModel.toData() = LocationDto(name)

fun CharacterFragment.Location.toData() = LocationDto(name)