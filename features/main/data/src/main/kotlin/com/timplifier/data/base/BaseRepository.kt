package com.timplifier.data.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.flowable
import com.timplifier.common.either.Either
import com.timplifier.data.utils.DataMapper
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


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
            Either.Left(throwable.message ?: "Error Occurred!")
        }
}


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

class daun {

}