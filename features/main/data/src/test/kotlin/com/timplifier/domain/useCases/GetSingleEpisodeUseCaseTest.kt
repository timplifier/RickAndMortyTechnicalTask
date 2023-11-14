package com.timplifier.domain.useCases

import app.cash.turbine.test
import com.timplifier.domain.models.EpisodeModel
import com.timplifier.domain.repositories.MockEpisodeRepository
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class GetSingleEpisodeUseCaseTest {
    private lateinit var getSingleEpisodeUseCase: GetSingleEpisodeUseCase
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
        getSingleEpisodeUseCase = GetSingleEpisodeUseCase(mockEpisodeRepository)
    }

    @Test
    fun `Get single episode by id`() = runTest {
        val episode = episodes.first()
        getSingleEpisodeUseCase(episode.url).test {
            Assert.assertEquals(episode, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}