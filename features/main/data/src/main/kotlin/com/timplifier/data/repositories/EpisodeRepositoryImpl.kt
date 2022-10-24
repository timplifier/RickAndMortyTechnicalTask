package com.timplifier.data.repositories

import com.timplifier.data.base.makeNetworkRequest
import com.timplifier.data.remote.apiservices.EpisodeApiService
import com.timplifier.domain.repository.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService
) : EpisodeRepository {

    override fun fetchSingleEpisode(id: Int) = makeNetworkRequest {
        episodeApiService.fetchSingleEpisode(id).toDomain()
    }
}