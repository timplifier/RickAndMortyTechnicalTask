package com.timplifier.main.presentation.ui.fragments.main

import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.timplifier.core.base.BaseFlowFragment
import com.timplifier.main.R

class MainFlowFragment :
    BaseFlowFragment(R.layout.fragment_main_flow, R.id.nav_host_fragment_main) {

    override fun setupNavigation(navController: NavController) {
        setLightAppearanceNavigationBar()
    }

    private fun setLightAppearanceNavigationBar() {
        WindowInsetsControllerCompat(
            requireActivity().window, requireView()
        ).isAppearanceLightNavigationBars = false
    }
}