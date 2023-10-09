package com.timplifier.core.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.viewbinding.ViewBinding
import com.timplifier.common.either.Either
import com.timplifier.core.ui.state.UIState
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(
    @LayoutRes layoutId: Int
) :
    Fragment(layoutId) {
    protected abstract val binding: Binding
    protected abstract val viewModel: ViewModel

    protected val disposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        assembleViews()
        constructListeners()
        establishRequest()
        launchObservers()
    }

    protected open fun initialize() {}

    protected open fun assembleViews() {}

    protected open fun constructListeners() {}

    protected open fun establishRequest() {}

    protected open fun launchObservers() {}

    protected fun <T : Any> Flowable<PagingData<T>>.spectatePaging(
        success: (data: PagingData<T>) -> Unit,
    ) {
        safeFlowableGather {
            success(this)
        }
    }

    protected fun <T> Observable<UIState<T>>.spectateUiState(
        success: ((data: T) -> Unit)? = null,
        loading: ((data: UIState.Loading<T>) -> Unit)? = null,
        error: ((error: String) -> Unit)? = null,
        idle: ((idle: UIState.Idle<T>) -> Unit)? = null,
        gatherIfSucceed: ((state: UIState<T>) -> Unit)? = null,
    ) {
        safeObservableGather {
            gatherIfSucceed?.invoke(this)
            when (this) {
                is UIState.Idle -> idle?.invoke(this)
                is UIState.Loading -> loading?.invoke(this)
                is UIState.Error -> error?.invoke(this.error)
                is UIState.Success -> success?.invoke(data)
            }
        }
    }

    fun <T> Observable<T>.safeObservableGather(success: T.() -> Unit) {
        disposable.add(observeOn(AndroidSchedulers.mainThread()).subscribe {
            success(it)
        })
    }

    fun <T> Observable<Either<String, T>>.safeObservableGather(
        actionIfEitherIsLeft: String.() -> Unit,
        actionIfEitherIsRight: T.() -> Unit
    ) {
        disposable.add(observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it) {
                is Either.Left -> actionIfEitherIsLeft(it.value)
                is Either.Right -> actionIfEitherIsRight(it.value)
            }
        })
    }

    fun <T> Flowable<T>.safeFlowableGather(success: T.() -> Unit) {
        disposable.add(observeOn(AndroidSchedulers.mainThread()).subscribe {
            success(it)
        })
    }

    protected fun <T> UIState<T>.assembleViewVisibility(
        group: Group,
        loader: ProgressBar,
        navigationSucceed: Boolean = false,
    ) {
        fun displayLoader(isDisplayed: Boolean) {
            group.isVisible = !isDisplayed
            loader.isVisible = isDisplayed
        }
        when (this) {
            is UIState.Idle -> {}
            is UIState.Loading -> displayLoader(true)
            is UIState.Error -> displayLoader(false)
            is UIState.Success -> {
                if (navigationSucceed) {
                    displayLoader(true)
                } else {
                    displayLoader(false)
                }
            }
        }
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }
}