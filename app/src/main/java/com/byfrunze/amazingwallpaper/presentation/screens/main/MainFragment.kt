package com.byfrunze.amazingwallpaper.presentation.screens.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.data.Wallpaper
import com.byfrunze.amazingwallpaper.presentation.helpers.injectViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : Fragment(R.layout.fragment_main) {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel

    private val adapter = MainAdapter()

    var id = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = injectViewModel(factory = viewModeFactory)



        adapter.imagesAdapterClicks = object : ImagesAdapterClicks {
            override fun onItemClick(model: Wallpaper, image: ImageView) {
                val extras = FragmentNavigatorExtras(image to model.url_image)
                val action =
                    MainFragmentDirections.actionMainFragmentToSetupFragment(model.url_image)
                findNavController().navigate(action, extras)
            }
        }

        var page = 2
        adapter.loadImages = object : OnLoadImages {
            override fun onLoad(load: Boolean) {
                if (load) mainViewModel.obtainEvent(SideEffect.LoadMoreWallpaper(page = page++, id = id))
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            id = it.getString("nameofcat", "1")
        }

        Log.i("RX", "ED $id")
        mainViewModel.apply {
            viewStates().observe(viewLifecycleOwner, Observer { bindViewState(it) })
            viewEffects().observe(viewLifecycleOwner, Observer { bindViewAction(it) })
        }
        mainViewModel.obtainEvent(viewEvent = SideEffect.ScreenShow(id = id))

        recycler_view_images_main.adapter = adapter
        recycler_view_images_main.layoutManager = GridLayoutManager(requireContext(), 3)

    }

    private fun bindViewState(viewState: MainViewState) {
        when (viewState.wallpaperStatus) {
            is WallpaperStatus.Loading -> loading()
            is WallpaperStatus.Success -> success(
                viewState.data ?: emptyList()
            )
            is WallpaperStatus.AddMoreWallpaper -> addMoreWallpaper(viewState.data ?: emptyList())

        }
    }

    private fun loading() {
        animation_view.visibility = View.VISIBLE
    }

    private fun success(arrayOfData: List<Wallpaper>) {
        animation_view.visibility = View.GONE
        adapter.initArray(arrayOfData)
    }

    private fun addMoreWallpaper(arrayOfData: List<Wallpaper>) {
        animation_view.visibility = View.GONE
        adapter.addImages(arrayOfData)
    }

    private fun bindViewAction(viewAction: MainAction) {
        when (viewAction) {
            is MainAction.ShowError -> {
                Toast.makeText(
                    requireContext(),
                    viewAction.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}