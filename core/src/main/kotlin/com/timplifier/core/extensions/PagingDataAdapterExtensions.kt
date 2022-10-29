package com.timplifier.core.extensions

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView

fun <T : Any, VH : RecyclerView.ViewHolder> PagingDataAdapter<T, VH>.bindViewsToPagingLoadStates(
    recyclerView: RecyclerView,
    progressBar: ProgressBar,
) {
    addLoadStateListener { loadState ->
        recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
        progressBar.isVisible = loadState.refresh is LoadState.Loading
    }
}