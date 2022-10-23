package com.timplifier.data.remote.dtos


import com.google.gson.annotations.SerializedName
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.OriginModel

data class OriginDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) : DataMapper<OriginModel> {
    override fun toDomain() = OriginModel(name, url)

}
