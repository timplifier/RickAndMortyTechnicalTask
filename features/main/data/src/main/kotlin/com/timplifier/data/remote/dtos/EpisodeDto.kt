package com.timplifier.data.remote.dtos


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.EpisodeModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class EpisodeDto(
    @SerialName("id")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("air_date")
    val airDate: String,
    @SerialName("episode")
    val episode: String,
    @SerialName("characters")
    val characters: List<String>,
    @SerialName("url")
    val url: String,
    @SerialName("created")
    val created: String
) : DataMapper<EpisodeModel> {
    override fun toDomain() = EpisodeModel(id, name, airDate, episode, characters, url, created)
}

fun EpisodeModel.toData() = EpisodeDto(id, name, airDate, episode, characters, url, created)