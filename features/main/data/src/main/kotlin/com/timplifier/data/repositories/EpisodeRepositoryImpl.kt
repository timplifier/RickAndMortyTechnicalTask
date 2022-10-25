package com.timplifier.data.repositories

import com.timplifier.data.base.makeNetworkRequest
import com.timplifier.data.local.db.daos.EpisodeDao
import com.timplifier.data.remote.apiservices.EpisodeApiService
import com.timplifier.domain.repositories.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val episodeDao: EpisodeDao
) : EpisodeRepository {

    override fun fetchSingleEpisode(id: Int) = makeNetworkRequest {
        episodeApiService.fetchSingleEpisode(id).also {
            episodeDao.insertEpisodes(it)
        }.toDomain()
    }
}