package com.byfrunze.amazingwallpaper.presentation.screens.tragetcategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.presentation.helpers.injectViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TargetCategoryFragment : Fragment(R.layout.fragment_target_category) {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private lateinit var targetCategoryViewModel: TargetCategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetCategoryViewModel = injectViewModel(factory = viewModeFactory)
    }


}