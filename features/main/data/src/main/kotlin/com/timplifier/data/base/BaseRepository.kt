package com.timplifier.data.base

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.flowable
import com.timplifier.common.either.Either
import com.timplifier.data.utils.DataMapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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

fun <T> makeRequest(
    gatherIfSucceed: ((T) -> Unit)? = null,
    request: () -> Single<T>
): Observable<Either<String, T>> {
    return Observable.create<Either<String, T>> { emitter ->
        request().subscribe({ result ->
            gatherIfSucceed?.invoke(result)
            emitter.onNext(Either.Right(value = result))
            emitter.onComplete()
        }, { throwable ->
            emitter.onNext(Either.Left(throwable.message ?: "Error Occurred!"))
            emitter.onComplete()
        })
    }
        .subscribeOn(Schedulers.io())
        .onErrorReturn { throwable ->
            Either.Left(throwable.message ?: "Error Occurred!")
        }
}

fun <T> makeRemoteRequest(
    gatherIfSucceed: ((T) -> Unit)? = null,
    request: () -> Observable<T>
): Observable<Either<String, T>> {
    return Observable.create<Either<String, T>> { emitter ->
        request().subscribe({ result ->
            gatherIfSucceed?.invoke(result)
            emitter.onNext(Either.Right(value = result))
            emitter.onComplete()
        }, { throwable ->
            emitter.onNext(Either.Left(throwable.message ?: "Error Occurred!"))
            emitter.onComplete()
        })
    }
        .subscribeOn(Schedulers.io())
        .onErrorReturn { throwable ->
            Log.e("gaypop", throwable.localizedMessage.toString() )
            Either.Left(throwable.message ?: "Error Occurred!")
        }
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

internal fun <ValueDto : DataMapper<Value>, Value : Any> makePagingRequest(
    pagingSource: BaseRxPagingSource<ValueDto, Value>,
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
    ).flowable