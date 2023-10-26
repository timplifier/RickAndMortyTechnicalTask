package com.timplifier.data.remote.dtos


import com.google.gson.annotations.SerializedName
import com.timplifier.data.fragment.CharacterFragment
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.OriginModel

data class OriginDto(
    @SerializedName("name")
    val name: String,
) : DataMapper<OriginModel> {
    override fun toDomain() = OriginModel(name)
}

fun OriginModel.toData() = OriginDto(name)

fun CharacterFragment.Origin.toData() = OriginDto(name)