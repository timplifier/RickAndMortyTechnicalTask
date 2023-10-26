package com.timplifier.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.timplifier.common.either.Either
import com.timplifier.core.ui.state.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

sealed class ViewModel<State : Any, SideEffect : Any>(initialState: State) :
    ContainerHost<State, SideEffect>, ViewModel() {
    override val container = container<State, SideEffect>(initialState = initialState)

    fun postSideEffect(sideEffect: SideEffect) = intent {
        postSideEffect(sideEffect)
    }

    protected fun <T, S> Flow<Either<String, T>>.gatherRequest(
        mappedData: (data: T) -> S,
        scope: suspend SimpleSyntax<State, SideEffect>.(data: Flow<UIState<S>>) -> Unit
    ) {
        containerScope {
            viewModelScope.launch(Dispatchers.IO) {
                scope(this@containerScope,
                    flow {
                        emit(UIState.Loading())
                        this@gatherRequest.collect {
                            when (it) {
                                is Either.Left -> emit(UIState.Error(it.value))
                                is Either.Right -> emit(
                                    UIState.Success(mappedData(it.value))
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    protected fun <T, S> Flow<T>.gatherLocalRequest(
        mappedData: (data: T) -> S,
        scope: suspend SimpleSyntax<State, SideEffect>.(data: Flow<UIState<S>>) -> Unit
    ) {
        containerScope {
            viewModelScope.launch(Dispatchers.IO) {
                scope(this@containerScope,
                    flow {
                        emit(UIState.Loading())
                        this@gatherLocalRequest.collect {
                            emit(UIState.Success(mappedData(it)))
                        }
                    }
                )
            }
        }
    }

    protected fun <T, S> Flow<T>.gatherLocalRequestNoUIState(
        mappedData: (data: T) -> S,
        scope: suspend SimpleSyntax<State, SideEffect>.(data: S) -> Unit
    ) {
        containerScope {
            viewModelScope.launch(Dispatchers.IO) {
                this@gatherLocalRequestNoUIState.collectLatest {
                    scope(this@containerScope, mappedData(it))
                }
            }
        }
    }

    protected fun containerScope(scope: suspend SimpleSyntax<State, SideEffect>.() -> Unit) {
        intent {
            scope()
        }
    }

    protected fun <T : Any, S : Any> Flow<PagingData<T>>.gatherPagingRequest(
        mappedData: (data: T) -> S,
        scope: suspend SimpleSyntax<State, SideEffect>.(data: PagingData<S>) -> Unit
    ) {
        containerScope {
            map {
                it.map { data -> mappedData(data) }
            }.cachedIn(viewModelScope).collectLatest { pagingData ->
                scope(this, pagingData)
            }
        }
    }

    abstract class NoStateViewModel<SideEffect : Any> :
        com.timplifier.core.base.ViewModel<Unit, SideEffect>(Unit)

    abstract class TurnViewModel<State : Any, Turn : Any, SideEffect : Any>(initialState: State) :
        com.timplifier.core.base.ViewModel<State, SideEffect>(initialState) {

        abstract fun processTurn(turn: Turn)
    }
}