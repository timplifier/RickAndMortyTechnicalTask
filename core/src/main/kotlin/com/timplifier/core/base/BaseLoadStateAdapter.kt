package com.timplifier.core.base

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class BaseLoadStateAdapter
    : LoadStateAdapter<BaseLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ) = BaseLoadStateViewHolder.create(parent)

    override fun onBindViewHolder(holder: BaseLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}