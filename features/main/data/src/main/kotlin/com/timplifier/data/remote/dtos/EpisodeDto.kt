package com.timplifier.data.remote.dtos


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.timplifier.data.fragment.CharacterFragment
import com.timplifier.data.utils.DataMapper
import com.timplifier.domain.models.EpisodeModel

@Entity
data class EpisodeDto(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode")
    val episode: String,
    @SerializedName("created")
    val created: String
) : DataMapper<EpisodeModel> {
    override fun toDomain() = EpisodeModel(id, name, airDate, episode, created)
}

fun EpisodeModel.toData() = EpisodeDto(id, name, airDate, episode, created)

fun CharacterFragment.Episode.toData() = EpisodeDto(
    id,
    name,
    airDate,
    episode,
    created
)