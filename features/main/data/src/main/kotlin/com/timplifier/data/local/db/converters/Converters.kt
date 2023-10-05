package com.timplifier.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timplifier.data.remote.dtos.LocationDto
import com.timplifier.data.remote.dtos.OriginDto

class Converters {

    private inline fun <reified T> typeToken() = object : TypeToken<T>() {}.type

    private inline fun <reified T> fromJson(value: String?): T =
        Gson().fromJson(value, typeToken<T>())

    private inline fun <reified T> toJson(generic: T) =
        Gson().toJson(generic, typeToken<T>())

    @TypeConverter
    fun fromOrigin(value: String?) = fromJson<OriginDto>(value)

    @TypeConverter
    fun toOrigin(origin: OriginDto): String = toJson(origin)

    @TypeConverter
    fun fromLocation(value: String?) = fromJson<LocationDto>(value)

    @TypeConverter
    fun toLocation(location: LocationDto): String = toJson(location)

    @TypeConverter
    fun fromEpisode(value: String?) = fromJson<List<String>>(value)

    @TypeConverter
    fun toEpisode(episodes: List<String>): String = toJson(episodes)
}