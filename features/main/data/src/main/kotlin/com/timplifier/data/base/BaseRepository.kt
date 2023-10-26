package com.timplifier.data.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.network.okHttpClient
import com.timplifier.common.either.Either
import com.timplifier.data.remote.provideOkHttpClientBuilder
import com.timplifier.data.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


internal val apolloClient =
    ApolloClient.Builder().serverUrl("https://rickandmortyapi.com/graphql")
        .dispatcher(Dispatchers.IO).okHttpClient(provideOkHttpClientBuilder().build())
        .build()

internal suspend fun <T : Query.Data> ApolloClient.executeQuery(query: Query<T>) =
    query(query).execute().dataAssertNoErrors

internal fun <T> makeNetworkRequest(
    gatherIfSucceed: ((T) -> Unit)? = null,
    request: suspend () -> T
) =
    flow<Either<String, T>> {
        request().also {
            gatherIfSucceed?.invoke(it)
            emit(Either.Right(value = it))
        }
    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Either.Left(value = exception.localizedMessage ?: "Error Occurred!"))
    }

internal fun <ValueDto : DataMapper<Value>, Value : Any> makePagingRequest(
    pagingSource: BasePagingSource<ValueDto, Value>,
    pageSize: Int = 20,
    prefetchDistance: Int = pageSize,
    enablePlaceholders: Boolean = true,
    initialLoadSize: Int = pageSize * 3,
    maxSize: Int = Int.MAX_VALUE,
    jumpThreshold: Int = Int.MIN_VALUE,
) =
    Pager(
        config = PagingConfig(
            pageSize,
            prefetchDistance,
            enablePlaceholders,
            initialLoadSize,
            maxSize,
            jumpThreshold
        ),
        pagingSourceFactory = {
            pagingSource
        }
    ).flow