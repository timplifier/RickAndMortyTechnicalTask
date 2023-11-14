package com.timplifier.domain.useCases

import app.cash.turbine.test
import com.timplifier.common.either.Either
import com.timplifier.domain.models.EpisodeModel
import com.timplifier.domain.repositories.MockEpisodeRepository
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class FetchSingleEpisodeUseCaseTest {
    private lateinit var fetchSingleEpisodeUseCase: FetchSingleEpisodeUseCase
    private lateinit var mockEpisodeRepository: MockEpisodeRepository

    private val json = Json {
        isLenient = true
    }

    private val episodes = json.decodeFromString<List<EpisodeModel>>(
        String(
            Files.readAllBytes(
                Paths.get(
                    this::class.java.classLoader.getResource("episodes.json").toURI()
                )
            ), StandardCharsets.UTF_8
        )
    )

    @Before
    fun setup() {
        mockEpisodeRepository = MockEpisodeRepository(episodes)
        fetchSingleEpisodeUseCase = FetchSingleEpisodeUseCase(mockEpisodeRepository)
    }

    @Test
    fun `Fetch single episode by id`() = runTest {
        val episode = episodes.first()
        fetchSingleEpisodeUseCase(episode.id).test {
            when (val fetchedEpisode = awaitItem()) {
                is Either.Left -> {
                    awaitError()
                }

                is Either.Right -> {
                    assertEquals(episode, fetchedEpisode.value)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }
}