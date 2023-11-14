package com.timplifier.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.timplifier.data.local.db.daos.EpisodeDao
import com.timplifier.data.remote.dtos.EpisodeDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class EpisodeDaoTest {
    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var episodeDao: EpisodeDao

    @Before
    fun setup() {
        rickAndMortyDatabase =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RickAndMortyDatabase::class.java
            ).allowMainThreadQueries().build()
        episodeDao = rickAndMortyDatabase.episodeDao()
    }

    @Test
    fun insertEpisode() = runTest {
        val episode =
            EpisodeDto(
                1,
                "",
                "",
                "",
                listOf(),
                "someUrl",
                ""
            )
        episodeDao.insertEpisodes(episode)
        assertThat(episodeDao.getSingleEpisode(episode.url).first()).isEqualTo(episode)
    }

    @After
    fun teardown() {
        rickAndMortyDatabase.close()
    }
}