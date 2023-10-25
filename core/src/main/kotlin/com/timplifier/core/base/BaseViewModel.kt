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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

sealed class ViewModel<State : Any, SideEffect : Any>(initialState: State) :
    ContainerHost<State, SideEffect>, ViewModel() {
    protected fun <T> mutableUiStateFlow() = MutableStateFlow<UIState<T>>(UIState.Idle())

    override val container = container<State, SideEffect>(initialState = initialState)

    fun postSideEffect(sideEffect: SideEffect) = intent {
        postSideEffect(sideEffect)
    }

    protected fun <T, S> Flow<Either<String, T>>.gatherRequest(
        state: MutableStateFlow<UIState<S>>,
        mappedData: (data: T) -> S,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@gatherRequest.collect {
                when (it) {
                    is Either.Left -> state.value = UIState.Error(it.value)
                    is Either.Right -> state.value =
                        UIState.Success(mappedData(it.value))
                }
            }
        }
    }

    protected fun <T> Flow<Either<String, T>>.gatherRequest(
        state: MutableStateFlow<UIState<T>>,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            state.value = UIState.Loading()
            this@gatherRequest.collect {
                when (it) {
                    is Either.Left -> state.value = UIState.Error(it.value)
                    is Either.Right -> state.value =
                        UIState.Success(it.value)
                }
            }
        }
    }

    protected fun <T : Any, S : Any> Flow<PagingData<T>>.gatherPagingRequest(
        mappedData: (data: T) -> S,
    ) = map {
        it.map { data -> mappedData(data) }
    }.cachedIn(viewModelScope)

    abstract class NoStateViewModel<SideEffect : Any> :
        com.timplifier.core.base.ViewModel<Unit, SideEffect>(Unit)

    abstract class TurnViewModel<State : Any, Turn : Any, SideEffect : Any>(initialState: State) :
        com.timplifier.core.base.ViewModel<State, SideEffect>(initialState) {
        abstract fun processTurn(turn: Turn)
    }
}