package com.byfrunze.amazingwallpaper.presentation.screens.setup

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.presentation.helpers.injectViewModel
import com.byfrunze.amazingwallpaper.presentation.viewstate.LoadStatus
import com.byfrunze.amazingwallpaper.presentation.viewstate.SetupAction
import com.byfrunze.amazingwallpaper.presentation.viewstate.SetupEffect
import com.byfrunze.amazingwallpaper.presentation.viewstate.SetupState
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private lateinit var setupViewModel: SetupViewModel
    private val MY_PERMISSIONS_REQUEST_CODE = 1

    var background: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel = injectViewModel(factory = viewModeFactory)
        val storage = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        background = arguments?.getString("url", "")

        Glide.with(view)
            .load(background)
            .optionalCenterCrop()
            .into(image_view_setup)

        text_view_download.setOnClickListener {
            requestPermission()
            if (storage == PackageManager.PERMISSION_GRANTED)
                background?.let { background ->
                    setupViewModel.obtainEvent(
                        SetupEffect.Download(
                            drawableUrl = background,
                            context = requireContext()
                        )
                    )
                }
        }
        text_view_setup_screen.setOnClickListener {
            background?.let { background ->
                setupViewModel.obtainEvent(
                    SetupEffect.SetupScreen(
                        drawableUrl = background,
                        context = requireContext()
                    )
                )
            }
        }
        text_view_setup_lock.setOnClickListener {
            background?.let { background ->
                setupViewModel.obtainEvent(
                    SetupEffect.SetupScreenLock(
                        drawableUrl = background,
                        context = requireContext()
                    )
                )
            }
        }
        image_view_toback.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(
                    requireView(),
                    "Необходимо Ваше согласие, чтобы сохранить изображение в галерею",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                this.requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    background?.let { background ->
                        setupViewModel.obtainEvent(
                            SetupEffect.Download(
                                drawableUrl = background,
                                context = requireContext()
                            )
                        )
                    }

                } else {
                    Snackbar.make(
                        requireView(),
                        "Для сохранения изображения необходим доступ",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel.apply {
            viewStates().observe(viewLifecycleOwner, Observer { bindViewState(it) })
            viewEffects().observe(viewLifecycleOwner, Observer { bindViewAction(it) })
        }
    }

    private fun bindViewAction(viewAction: SetupAction) {
        when (viewAction) {
            is SetupAction.ShowSnackBar -> {
                Snackbar.make(requireView(), viewAction.text, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun bindViewState(viewState: SetupState) {
        when (viewState.loadStatus) {
            is LoadStatus.Loading -> {

            }
            is LoadStatus.Success -> {

            }
            is LoadStatus.Error -> {

            }
        }
    }


}