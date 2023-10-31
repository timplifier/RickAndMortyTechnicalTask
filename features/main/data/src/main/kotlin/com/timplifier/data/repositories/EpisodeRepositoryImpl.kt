package com.timplifier.data.repositories

import com.timplifier.data.base.makeNetworkRequest
import com.timplifier.data.local.db.realms.EpisodeRealm
import com.timplifier.data.remote.apiservices.EpisodeApiService
import com.timplifier.data.remote.dtos.toRealm
import com.timplifier.domain.repositories.EpisodeRepository
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val episodeRealm: EpisodeRealm
) : EpisodeRepository {

    override fun fetchSingleEpisode(id: Int) = makeNetworkRequest {
        episodeApiService.fetchSingleEpisode(id).also {
            episodeRealm.writeEpisode(it.toRealm())
        }.toDomain()
    }

    override fun getSingleEpisode(url: String) =
        episodeRealm.queryEpisode(url)
}