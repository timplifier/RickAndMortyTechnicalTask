package com.timplifier.data.local.db.dtos

import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.OriginModel
import io.realm.kotlin.types.EmbeddedRealmObject
import kotlinx.serialization.SerialName

class OriginRealmDto(
    @SerialName("name")
    var name: String,
    @SerialName("url")
    var url: String
) : DataMapper<OriginModel>, EmbeddedRealmObject {
    override fun toDomain() = OriginModel(name, url)

    constructor() : this(
        name = "",
        url = ""
    )
}