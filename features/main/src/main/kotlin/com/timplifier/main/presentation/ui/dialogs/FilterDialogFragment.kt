package com.timplifier.main.presentation.ui.dialogs

import android.content.Context
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.common.constants.Constants.CHARACTER_GENDER_QUERY
import com.timplifier.common.constants.Constants.CHARACTER_SPECIES_QUERY
import com.timplifier.common.constants.Constants.CHARACTER_STATUS_QUERY
import com.timplifier.core.base.BaseDialogFragment
import com.timplifier.core.extensions.directionsSafeNavigation
import com.timplifier.core.extensions.setOnCheckedChangeListenerAndRetrieveItsText
import com.timplifier.core.utils.ViewModelFactory
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentFilterDialogBinding
import com.timplifier.main.presentation.di.components.DaggerMainComponent
import com.timplifier.main.presentation.models.CharacterFilter
import com.timplifier.main.presentation.models.states.filter.FilterSideEffect
import org.orbitmvi.orbit.viewmodel.observe
import javax.inject.Inject

class FilterDialogFragment :
    BaseDialogFragment<FragmentFilterDialogBinding, FilterViewModel>(R.layout.fragment_filter_dialog) {
    override val binding by viewBinding(FragmentFilterDialogBinding::bind)
    override val viewModel by viewModels<FilterViewModel> {
        ViewModelProvider(this, viewModelFactory)[FilterViewModel::class.java]
        viewModelFactory
    }

    private val characterFilter = CharacterFilter()
    private val args by navArgs<FilterDialogFragmentArgs>()

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        DaggerMainComponent.builder().context(context).build().inject(this)
        super.onAttach(context)
    }

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

    private fun resetFilters() {
        binding.tvResetFilters.setOnClickListener {
            viewModel.postSideEffect(FilterSideEffect.ResetFilters)
        }
    }

    private fun applyFilters() {
        binding.tvApplyFilters.setOnClickListener {
            viewModel.postSideEffect(FilterSideEffect.ApplyFilters(characterFilter))
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
                } ?: also {
                    binding.rgStatus.clearCheck()
                    binding.rgSpecies.clearCheck()
                    binding.rgGender.clearCheck()
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

    override fun setupObservers() {
        viewModel.observe(viewLifecycleOwner, sideEffect = ::handleSideEffect)
    }

    private fun handleSideEffect(filterSideEffect: FilterSideEffect) {
        when (filterSideEffect) {
            is FilterSideEffect.ApplyFilters -> {
                findNavController().directionsSafeNavigation(
                    FilterDialogFragmentDirections.actionFilterDialogFragmentToCharactersFragment(
                        filterSideEffect.characterFilter
                    )
                )
            }

            FilterSideEffect.ResetFilters -> {
                binding.apply {
                    rgStatus.clearCheck()
                    rgSpecies.clearCheck()
                    rgGender.clearCheck()
                }
                characterFilter.status = null
                characterFilter.species = null
                characterFilter.gender = null
            }
        }
    }
}