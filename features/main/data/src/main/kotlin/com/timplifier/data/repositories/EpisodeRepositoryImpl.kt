package com.timplifier.data.repositories

import com.timplifier.data.base.makeRequest
import com.timplifier.data.local.db.daos.EpisodeDao
import com.timplifier.data.remote.apiservices.EpisodeApiService
import com.timplifier.domain.models.EpisodeModel
import com.timplifier.domain.repositories.EpisodeRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val episodeApiService: EpisodeApiService,
    private val episodeDao: EpisodeDao
) : EpisodeRepository {

    override fun fetchSingleEpisode(id: Int) = makeRequest {
        episodeApiService.fetchSingleEpisode(id).also {
            it.subscribeOn(Schedulers.io()).subscribe { result ->
                episodeDao.insertEpisodes(result)
            }
        }.map { it.toDomain() }
    }

    override fun getSingleEpisode(url: String): Observable<EpisodeModel> =
        episodeDao.getSingleEpisode(url).map { it.toDomain() }
}