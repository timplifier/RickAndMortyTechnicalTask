package com.timplifier.domain.repositories

import com.timplifier.common.either.Either
import com.timplifier.domain.models.EpisodeModel
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun fetchSingleEpisode(id: Int): Flow<Either<String, EpisodeModel>>
    fun getSingleEpisode(url: String): Flow<EpisodeModel>
}