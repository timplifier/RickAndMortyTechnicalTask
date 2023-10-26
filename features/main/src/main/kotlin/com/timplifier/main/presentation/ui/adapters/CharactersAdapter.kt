package com.timplifier.main.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.timplifier.core.base.BaseDiffUtil
import com.timplifier.core.extensions.loadImageWithGlide
import com.timplifier.main.R
import com.timplifier.main.databinding.ItemCharacterBinding
import com.timplifier.main.presentation.models.CharacterUI

class CharactersAdapter(
    private val onItemClick: (id: String) -> Unit,
) :
    PagingDataAdapter<CharacterUI, CharactersAdapter.CharactersViewHolder>(BaseDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharactersViewHolder(
        ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    inner class CharactersViewHolder(private val binding: ItemCharacterBinding) :
        ViewHolder(binding.root) {
        fun onBind(character: CharacterUI) = with(binding) {
            character.apply {
                imCharacter.loadImageWithGlide(image)
                tvCharacterName.text = name
                when (status) {
                    "Alive" -> imStatus.setImageResource(R.drawable.ic_character_alive_status)
                    "Dead" -> imStatus.setImageResource(R.drawable.ic_character_dead_status)
                    "unknown" -> imStatus.setImageResource(R.drawable.ic_character_unknown_status)
                }
                tvCharacterStatusAndSpecies.text =
                    tvCharacterStatusAndSpecies.context.getString(
                        R.string.character_status_and_species_with_hyphen,
                        status,
                        species
                    )
                tvLastKnownLocationName.text = location.name
                tvFirstSeenInEpisode.text = episode.first().name
            }
        }

        init {
            binding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { character ->
                    onItemClick(character.id)
                }
            }
        }
    }
}