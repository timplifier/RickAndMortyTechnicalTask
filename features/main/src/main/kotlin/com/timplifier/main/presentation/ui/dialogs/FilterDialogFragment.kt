package com.timplifier.main.presentation.ui.fragments.main.characters.filter

import android.widget.RadioButton
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseDialogFragmentWithoutViewModel
import com.timplifier.core.extensions.directionsSafeNavigation
import com.timplifier.core.extensions.setOnCheckedChangeListenerAndRetrieveItsText
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentFilterDialogBinding
import com.timplifier.main.presentation.models.CharacterFilter

class FilterDialogFragment :
    BaseDialogFragmentWithoutViewModel<FragmentFilterDialogBinding>(R.layout.fragment_filter_dialog) {
    override val binding by viewBinding(FragmentFilterDialogBinding::bind)
    private val characterFilter = CharacterFilter()

    override fun setupListeners() = with(binding) {
        listenToStatusChanges()
        listenToSpeciesChanges()
        listenToGenderChanges()
        applyFilters()
    }

    private fun listenToStatusChanges() {
        listenToRadioGroupCheckedStatusAndRetrieveText(
            "status",
            binding.rbAlive,
            binding.rbDead,
            binding.rbUnknown,
        )
    }

    private fun listenToSpeciesChanges() {
        listenToRadioGroupCheckedStatusAndRetrieveText(
            "species",
            binding.rbHuman,
            binding.rbHumanoid,
            binding.rbAlien,
        )
    }

    private fun listenToGenderChanges() {
        listenToRadioGroupCheckedStatusAndRetrieveText(
            "gender",
            binding.rbMale,
            binding.rbFemale,
            binding.rbUnknownGender,
        )
    }

    private fun applyFilters() {
        binding.tvApplyFilters.setOnClickListener {
            findNavController().directionsSafeNavigation(
                FilterDialogFragmentDirections.actionFilterDialogFragmentToCharactersFragment(
                    characterFilter
                )
            )
        }
    }

    private fun listenToRadioGroupCheckedStatusAndRetrieveText(
        fieldToChange: String,
        vararg radioButtons: RadioButton,
    ) {
        radioButtons.forEach {
            it.setOnCheckedChangeListenerAndRetrieveItsText { radioButtonText ->
                when (fieldToChange) {
                    "status" -> characterFilter.status = radioButtonText
                    "species" -> characterFilter.species = radioButtonText
                    "gender" -> characterFilter.gender = radioButtonText
                }
            }
        }
    }
}