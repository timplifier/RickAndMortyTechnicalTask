package com.timplifier.domain.useCases

import com.timplifier.domain.repositories.EpisodeRepository
import javax.inject.Inject

class GetSingleEpisodeUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {
    operator fun invoke(url: String) = episodeRepository.getSingleEpisode(url)
}