package com.timplifier.data.local.db.dtos

import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.LocationModel
import io.realm.kotlin.types.EmbeddedRealmObject
import kotlinx.serialization.SerialName

class LocationRealmDto(
    @SerialName("name")
    var name: String,
    @SerialName("url")
    var url: String
) : DataMapper<LocationModel>, EmbeddedRealmObject {
    override fun toDomain(): LocationModel {
        return LocationModel(name, url)
    }

    constructor() : this(
        name = "",
        url = ""
    )
}
