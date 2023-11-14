package com.timplifier.domain.useCases

import app.cash.turbine.test
import com.timplifier.common.either.Either
import com.timplifier.domain.models.CharacterModel
import com.timplifier.domain.repositories.MockCharacterRepository
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class FetchSingleCharacterUseCaseTest {
    private lateinit var fetchSingleCharacterUseCase: FetchSingleCharacterUseCase
    private lateinit var mockCharacterRepository: MockCharacterRepository

    private val json = Json {
        isLenient = true
    }

    private val characters = json.decodeFromString<List<CharacterModel>>(
        String(
            Files.readAllBytes(
                Paths.get(
                    this::class.java.classLoader.getResource("characters.json").toURI()
                )
            ), StandardCharsets.UTF_8
        )
    )

    @Before
    fun setup() {
        mockCharacterRepository = MockCharacterRepository(characters)
        fetchSingleCharacterUseCase = FetchSingleCharacterUseCase(mockCharacterRepository)
    }

    @Test
    fun `Fetch single character by id`() = runTest {
        val character = characters.first()
        fetchSingleCharacterUseCase(character.id).test {
            when (val fetchedCharacter = awaitItem()) {
                is Either.Left -> {
                    awaitError()
                }

                is Either.Right -> {
                    assertEquals(character, fetchedCharacter.value)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }
}