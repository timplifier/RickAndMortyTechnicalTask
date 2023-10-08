package com.timplifier.domain.repositories

import com.timplifier.common.either.Either
import com.timplifier.domain.models.EpisodeModel
import io.reactivex.Observable

interface EpisodeRepository {
    fun fetchSingleEpisode(id: Int): Observable<Either<String, EpisodeModel>>
    fun getSingleEpisode(url: String): Observable<EpisodeModel>
}