package com.timplifier.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.timplifier.data.local.db.daos.CharacterDao
import com.timplifier.data.remote.dtos.CharacterDto
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CharacterDaoTest {
    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var characterDao: CharacterDao

    private val json = Json {
        isLenient = true
    }

    private val characters =
        json.decodeFromString<List<CharacterDto>>(InstrumentationRegistry.getInstrumentation().context.assets.open(
            "characters.json"
        ).bufferedReader().use { it.readText() })

    private fun insertCharacters(
        nameQueryParameter: String? = null,
        statusQueryParameter: String? = null,
        speciesQueryParameter: String? = null,
        genderQueryParameter: String? = null,
        assertion: suspend TurbineTestContext<List<CharacterDto>> .(insertedCharacters: List<CharacterDto>) -> Unit
    ) = runTest {
        characterDao.insertCharacters(*characters.toTypedArray())
        characterDao.getCharacters(
            nameQueryParameter,
            statusQueryParameter,
            speciesQueryParameter,
            genderQueryParameter
        ).test {
            assertion(characters)
        }
    }

    @Before
    fun setup() {
        rickAndMortyDatabase =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RickAndMortyDatabase::class.java
            ).allowMainThreadQueries().build()
        characterDao = rickAndMortyDatabase.characterDao()
    }

    @Test
    fun insertCharacters() {
        insertCharacters {
            assertEquals(it, awaitItem())
        }
    }

    @Test
    fun insertCharactersAndQueryByName() {
        insertCharacters(
            nameQueryParameter = "Rick"
        ) { characters ->
            println(characters.toString())
            assertEquals(
                characters.filter { character -> character.name.contains("Rick") },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByStatus() {
        insertCharacters(statusQueryParameter = "Alive") { characters ->
            assertEquals(
                characters.filter { character -> character.status == "Alive" },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByGender() {
        insertCharacters(
            genderQueryParameter = "unknown"
        ) { characters ->
            println(characters.toString())
            assertEquals(
                characters.filter { character -> character.gender == "unknown" },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryBySpecies() {
        insertCharacters(speciesQueryParameter = "Alien") { characters ->
            assertEquals(
                characters.filter { character -> character.species == "Alien" },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByNameAndStatus() {
        insertCharacters(
            nameQueryParameter = "Rick",
            statusQueryParameter = "Alive"
        ) { characters ->
            assertEquals(
                characters.filter { character ->
                    character.name.contains("Rick") && character.status == "Alive"
                },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByNameAndGender() {
        insertCharacters(
            nameQueryParameter = "Rick",
            genderQueryParameter = "Male"
        ) { characters ->
            assertEquals(
                characters.filter { character ->
                    character.name.contains("Rick") && character.gender == "Male"
                },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByStatusAndSpecies() {
        insertCharacters(
            statusQueryParameter = "Alive",
            speciesQueryParameter = "Human"
        ) { characters ->
            assertEquals(
                characters.filter { character ->
                    character.status == "Alive" && character.species == "Human"
                },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByGenderAndSpecies() {
        insertCharacters(
            genderQueryParameter = "Female",
            speciesQueryParameter = "Alien"
        ) { characters ->
            assertEquals(
                characters.filter { character ->
                    character.gender == "Female" && character.species == "Alien"
                },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharactersAndQueryByAllParameters() {
        insertCharacters(
            nameQueryParameter = "Rick",
            statusQueryParameter = "Alive",
            speciesQueryParameter = "Human",
            genderQueryParameter = "Male"
        ) { characters ->
            assertEquals(
                characters.filter { character ->
                    character.name.contains("Rick") && character.status == "Alive" &&
                            character.species == "Human" && character.gender == "Male"
                },
                awaitItem()
            )
        }
    }

    @Test
    fun insertCharacter() = runTest {
        val character = characters.first()
        characterDao.insertCharacters(
            character
        )

        characterDao.getSingleCharacter(character.id).test {
            assertEquals(character, awaitItem())
        }
    }

    @After
    fun teardown() {
        rickAndMortyDatabase.close()
    }
}