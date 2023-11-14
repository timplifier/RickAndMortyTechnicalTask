package com.timplifier.domain.repositories

import com.timplifier.common.either.Either
import com.timplifier.domain.models.EpisodeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockEpisodeRepository(private val episodes: List<EpisodeModel>) : EpisodeRepository {

    override fun fetchSingleEpisode(id: Int): Flow<Either<String, EpisodeModel>> {
        return flow {
            val fetchedEpisode = episodes.find { it.id == id }
            emit(if (fetchedEpisode != null) Either.Right(fetchedEpisode) else Either.Left("Episode with provided is not found"))
        }
    }

    override fun getSingleEpisode(url: String): Flow<EpisodeModel> {
        return flow { emit(episodes.find { it.url == url }!!) }
    }
}