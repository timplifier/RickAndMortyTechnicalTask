package com.timplifier.domain.useCases

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
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

class GetCharactersUseCaseTest {

    private lateinit var getCharactersUseCase: GetCharactersUseCase
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

    private fun query(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null,
        assertion: suspend TurbineTestContext<List<CharacterModel>>.(characters: List<CharacterModel>) -> Unit
    ) = runTest {
        getCharactersUseCase(name, status, species, gender).test {
            assertion(characters)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Before
    fun setup() {
        mockCharacterRepository = MockCharacterRepository(characters)
        getCharactersUseCase = GetCharactersUseCase(mockCharacterRepository)
    }

    @Test
    fun `Query characters by name`() {
        query("Rick") { characters ->
            assertEquals(
                characters.filter { character -> character.name.contains("Rick") },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by status`() {
        query(status = "Alive") { characters ->
            assertEquals(
                characters.filter { character -> character.status == "Alive" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by species`() {
        query(species = "Alien") { characters ->
            assertEquals(
                characters.filter { character -> character.species == "Alien" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by gender`() {
        query(gender = "unknown") { characters ->
            assertEquals(
                characters.filter { character -> character.gender == "unknown" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by name and status`() {
        query(name = "Rick", status = "Alive") { characters ->
            assertEquals(
                characters.filter { character -> character.name.contains("Rick") && character.status == "Alive" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by name and gender`() {
        query(name = "Rick", gender = "unknown") { characters ->
            assertEquals(
                characters.filter { character -> character.name.contains("Rick") && character.gender == "unknown" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by status and species`() {
        query(status = "Alive", species = "Alien") { characters ->
            assertEquals(
                characters.filter { character -> character.status == "Alive" && character.species == "Alien" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by species and gender`() {
        query(species = "Alien", gender = "unknown") { characters ->
            assertEquals(
                characters.filter { character -> character.species == "Alien" && character.gender == "unknown" },
                awaitItem()
            )
        }
    }

    @Test
    fun `Query characters by all parameters`() {
        query("Rick", "Alive", "Human", "Male") { characters ->
            assertEquals(
                characters.filter { character ->
                    character.name.contains("Rick") && character.status == "Alive" &&
                            character.species == "Human" && character.gender == "Male"
                },
                awaitItem()
            )
        }
    }
}