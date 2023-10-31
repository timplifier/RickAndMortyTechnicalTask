package com.timplifier.data.local.db.realms

import com.timplifier.data.extensions.queryList
import com.timplifier.data.extensions.querySingleObject
import com.timplifier.data.extensions.writeList
import com.timplifier.data.local.db.dtos.CharacterRealmDto
import com.timplifier.domain.models.CharacterModel
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow

class CharacterRealm(private val realm: Realm) {

    suspend fun writeCharacters(characters: List<CharacterRealmDto>) {
        realm.writeList(characters)
    }

    fun queryCharacter(id: Int) = realm.querySingleObject<CharacterRealmDto, CharacterModel>(
        query = "id = $0", id
    )

    fun queryCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<List<CharacterModel>> {
        val conditions = mutableListOf<String>()
        val args = mutableListOf<Any?>()

        name?.let {
            conditions.add("name CONTAINS[c] $0")
            args.add(it)
        }

        status?.let {
            conditions.add("status = $${args.size}")
            args.add(it)
        }

        species?.let {
            conditions.add("species = $${args.size}")
            args.add(it)
        }

        gender?.let {
            conditions.add("gender = $${args.size}")
            args.add(it)
        }

        val queryString = conditions.joinToString(" AND ")

        return realm.queryList<CharacterRealmDto, CharacterModel>(
            query = queryString,
            *args.toTypedArray()
        )
    }
}