package com.timplifier.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import com.timplifier.common.either.Either
import com.timplifier.core.ui.state.UIState
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi

abstract class BaseViewModel : ViewModel() {

    protected val disposable = CompositeDisposable()

    protected fun <T> uiBehaviorSubject() =
        BehaviorSubject.createDefault<UIState<T>>(UIState.Idle())

    protected fun <T, S> Observable<Either<String, T>>.gatherRequest(
        observable: BehaviorSubject<UIState<S>>,
        mappedData: (data: T) -> S,
    ) {
        disposable.add(subscribeOn(Schedulers.io()).subscribe { result ->
            observable.onNext(UIState.Loading())
            when (result) {
                is Either.Left -> observable.onNext(UIState.Error(result.value))
                is Either.Right -> observable.onNext(
                    UIState.Success(mappedData(result.value))
                )
            }
        })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <T : Any, S : Any> Flowable<PagingData<T>>.gatherPagingRequest(
        mappedData: (data: T) -> S,
    ) = map { it.map { data -> mappedData(data) } }.cachedIn(viewModelScope)

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}