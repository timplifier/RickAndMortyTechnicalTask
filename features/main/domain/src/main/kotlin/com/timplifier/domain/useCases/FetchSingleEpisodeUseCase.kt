package com.timplifier.domain.useCases

import com.timplifier.domain.repositories.EpisodeRepository
import javax.inject.Inject

class FetchSingleEpisodeUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    operator fun invoke(id: Int) = episodeRepository.fetchSingleEpisode(id)
}