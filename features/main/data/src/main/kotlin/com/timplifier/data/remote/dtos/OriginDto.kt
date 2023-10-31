package com.timplifier.data.remote.dtos


import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.OriginModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OriginDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
) : DataMapper<OriginModel> {
    override fun toDomain() = OriginModel(name, url)
}

fun OriginModel.toData() = OriginDto(name, url)