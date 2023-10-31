package com.timplifier.data.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.timplifier.common.either.Either
import com.timplifier.data.utils.DataMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


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

internal suspend inline fun <reified T> get(
    httpClient: HttpClient,
    crossinline url: URLBuilder.() -> Unit
) =
    httpClient.get {
        url {
            url()
        }
    }.body<T>()


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