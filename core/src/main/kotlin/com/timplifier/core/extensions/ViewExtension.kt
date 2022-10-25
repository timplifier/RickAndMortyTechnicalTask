package com.timplifier.core.extensions

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.geektechkb.core.ui.state.UIState

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun <T> ProgressBar.bindToUILoadState(uiState: UIState<T>) {
    isVisible = uiState is UIState.Loading
}

fun makeMultipleViewsInvisible(vararg views: View) {
    views.forEach {
        it.invisible()
    }
}