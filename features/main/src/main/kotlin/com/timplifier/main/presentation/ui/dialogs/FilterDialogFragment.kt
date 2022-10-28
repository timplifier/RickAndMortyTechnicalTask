package com.timplifier.main.presentation.ui.dialogs

import android.widget.RadioButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.common.constants.Constants.CHARACTER_GENDER_QUERY
import com.timplifier.common.constants.Constants.CHARACTER_SPECIES_QUERY
import com.timplifier.common.constants.Constants.CHARACTER_STATUS_QUERY
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
    private val args by navArgs<FilterDialogFragmentArgs>()

    override fun initialize() {
        modifyDialogCancellability()
    }

    private fun modifyDialogCancellability() {
        requireDialog().setCancelable(true)
        requireDialog().setCanceledOnTouchOutside(true)
    }

    override fun setupListeners() {
        listenToStatusChanges()
        listenToSpeciesChanges()
        listenToGenderChanges()
        resetFilters()
        applyFilters()
    }

    private fun listenToStatusChanges() {
        listenToRadioGroupCheckedStatusAndRetrieveText(
            CHARACTER_STATUS_QUERY,
            binding.rbAlive,
            binding.rbDead,
            binding.rbUnknown,
        )
    }

    private fun listenToSpeciesChanges() {
        listenToRadioGroupCheckedStatusAndRetrieveText(
            CHARACTER_SPECIES_QUERY,
            binding.rbHuman,
            binding.rbHumanoid,
            binding.rbAlien,
        )
    }

    private fun listenToGenderChanges() {
        listenToRadioGroupCheckedStatusAndRetrieveText(
            CHARACTER_GENDER_QUERY,
            binding.rbMale,
            binding.rbFemale,
            binding.rbUnknownGender,
        )
    }

    private fun resetFilters() = with(binding) {
        tvResetFilters.setOnClickListener {
            rgStatus.clearCheck()
            rgSpecies.clearCheck()
            rgGender.clearCheck()
            characterFilter.status = null
            characterFilter.species = null
            characterFilter.gender = null
        }
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
        radioButtons.forEach { radioButton ->
            characterFilter.apply {
                args.currentFilter?.let { previousFilter ->
                    when (fieldToChange) {
                        CHARACTER_STATUS_QUERY -> {
                            radioButton.isChecked =
                                radioButton.text.toString() == previousFilter.status
                            status = previousFilter.status
                        }

                        CHARACTER_SPECIES_QUERY -> {
                            radioButton.isChecked =
                                radioButton.text.toString() == previousFilter.species
                            species = previousFilter.species
                        }

                        CHARACTER_GENDER_QUERY -> {
                            radioButton.isChecked =
                                radioButton.text.toString() == previousFilter.gender
                            gender = previousFilter.gender
                        }
                    }
                }
                radioButton.setOnCheckedChangeListenerAndRetrieveItsText { radioButtonText ->
                    when (fieldToChange) {
                        CHARACTER_STATUS_QUERY ->
                            status = radioButtonText
                        CHARACTER_SPECIES_QUERY ->
                            species = radioButtonText
                        CHARACTER_GENDER_QUERY ->
                            gender = radioButtonText
                    }
                }
            }
        }
    }
}