package com.timplifier.core.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.timplifier.core.R
import com.timplifier.core.databinding.ItemLoadStateFooterBinding

class BaseLoadStateViewHolder(
    private val binding: ItemLoadStateFooterBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.cpi.isVisible = loadState is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup): BaseLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer, parent, false)
            val binding = ItemLoadStateFooterBinding.bind(view)
            return BaseLoadStateViewHolder(binding)
        }
    }
}