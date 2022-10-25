package com.timplifier.main.presentation.ui.fragments.main

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import androidx.navigation.NavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.timplifier.core.base.BaseFlowFragment
import com.timplifier.main.R
import com.timplifier.main.databinding.FragmentMainFlowBinding

class MainFlowFragment :
    BaseFlowFragment(R.layout.fragment_main_flow, R.id.nav_host_fragment_main) {
    private val binding by viewBinding(FragmentMainFlowBinding::bind)

    override fun setupNavigation(navController: NavController) {
        navigateBack(navController)
        renderToolbarElementsVisibilityAccordingToDestination(navController)
        modifySearchView()
        setLightAppearanceNavigationBar()
        openFilter(navController)
    }

    private fun navigateBack(navController: NavController) {
        binding.mtMainFlow.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    private fun renderToolbarElementsVisibilityAccordingToDestination(
        navController: NavController,
    ) = with(binding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destination.apply {
                imFilter.isGone = id == R.id.characterDetailFragment
                svCharacters.isGone = id == R.id.characterDetailFragment
                when (id == R.id.characterDetailFragment) {
                    true -> mtMainFlow.setNavigationIcon(R.drawable.ic_back)
                    false -> mtMainFlow.navigationIcon = null
                }
            }
        }
    }

    private fun modifySearchView() {
        val searchView: EditText =
            binding.svCharacters.findViewById(androidx.appcompat.R.id.search_src_text)
        searchView.setHintTextColor(Color.WHITE)
        val searchClearButton: ImageView =
            binding.svCharacters.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchClearButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
    }

    private fun setLightAppearanceNavigationBar() {
        WindowInsetsControllerCompat(
            requireActivity().window, requireView()
        ).isAppearanceLightNavigationBars = false
    }

    private fun openFilter(navController: NavController) {
        binding.imFilter.setOnClickListener {
            navController.navigate(R.id.filterDialogFragment)
        }
    }
}