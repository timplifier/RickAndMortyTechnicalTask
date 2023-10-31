package com.timplifier.data.local.db.dtos

import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.CharacterModel
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import org.mongodb.kbson.ObjectId

class CharacterRealmDto(
    @PrimaryKey
    var primaryKey: ObjectId,
    @SerialName("id")
    var id: Int,
    @SerialName("name")
    var name: String,
    @SerialName("status")
    var status: String,
    @SerialName("species")
    var species: String,
    @SerialName("type")
    var type: String,
    @SerialName("gender")
    var gender: String,
    @SerialName("origin")
    var origin: OriginRealmDto? = null,
    @SerialName("location")
    var location: LocationRealmDto? = null,
    @SerialName("image")
    var image: String,
    @SerialName("episode")
    var episode: RealmList<String>,
    @SerialName("url")
    var url: String,
    @SerialName("created")
    var created: String
) : DataMapper<CharacterModel>, RealmObject {
    override fun toDomain() = CharacterModel(
        id,
        name,
        status,
        species,
        type,
        gender,
        origin!!.toDomain(),
        location!!.toDomain(),
        image,
        episode,
        url,
        created
    )

    constructor() : this(
        primaryKey = ObjectId.invoke(),
        id = -1,
        name = "",
        status = "",
        species = "",
        type = "",
        gender = "",
        origin = OriginRealmDto(),
        location = LocationRealmDto(),
        image = "",
        episode = realmListOf<String>(),
        url = "",
        created = ""
    )
}