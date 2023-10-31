package com.timplifier.data.extensions

import com.timplifier.data.utils.DataMapper
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.TRUE_PREDICATE
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.map


internal suspend inline fun <reified T : RealmObject> Realm.writeSingleObject(value: T) {
    write {
        copyToRealm(value)
    }
}

internal suspend inline fun <reified T : RealmObject> Realm.writeList(value: List<T>) {
    write {
        value.map { copyToRealm(it) }
    }
}

internal inline fun <reified T, S> Realm.queryList(
    query: String = TRUE_PREDICATE,
    vararg args: Any?
) where T : RealmObject, T : DataMapper<S> =
    query<T>(query, *args).find().asFlow().map { it.list.map { dto -> dto.toDomain() } }

internal inline fun <reified T, S> Realm.querySingleObject(
    query: String = TRUE_PREDICATE,
    vararg args: Any?
) where T : RealmObject, T : DataMapper<S> =
    query<T>(query, *args).find().asFlow().map { it.list.first().toDomain() }