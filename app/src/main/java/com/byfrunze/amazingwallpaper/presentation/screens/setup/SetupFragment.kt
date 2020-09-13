package com.byfrunze.amazingwallpaper.presentation.screens.setup

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Menu
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.presentation.helpers.injectViewModel
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    val args: SetupFragmentArgs by navArgs()

    private lateinit var setupViewModel: SetupViewModel
    private val MY_PERMISSIONS_REQUEST_CODE = 1

    var background: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setEnterTransition()
        setHasOptionsMenu(true)
        requireActivity().text_view_title.text = "Setup"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel = injectViewModel(factory = viewModeFactory)
        val storage = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(requireContext(), this)

        setEnterTransition()
        postponeEnterTransition()
        val args = args.uri
        background = args
        image_view_setup.apply {
            transitionName = args
            Glide.with(this)
                .load(args)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalCenterCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(this)
        }

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
                animation_view.visibility = View.VISIBLE
            }
            is LoadStatus.Success -> {
                animation_view.visibility = View.GONE
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search).isVisible = false
    }

    private fun setEnterTransition() {
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}