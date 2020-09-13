package com.byfrunze.amazingwallpaper.presentation.screens.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search.*
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private lateinit var searchViewModel: SearchViewModel

    private val adapter = SearchAdapter()

    private var term = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search).setVisible(false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = injectViewModel(factory = viewModeFactory)

        adapter.imagesAdapterClicks = object : ImagesAdapterClicks {
            override fun onItemClick(model: Wallpaper, image: ImageView) {
                val extras = FragmentNavigatorExtras(image to model.url_image)
                val action =
                    SearchFragmentDirections.actionSearchFragmentToSetupFragment(model.url_image)
                findNavController().navigate(action, extras)
            }
        }
        var page = 2

        adapter.loadImages = object : OnLoadImages {
            override fun onLoad(load: Boolean) {
                if (load) searchViewModel.obtainEvent(
                    SearchEffect.LoadMoreWallpaper(
                        page = page++,
                        term = term
                    )
                )
            }
        }


        edit_text_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                Log.i("RX", "BE")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                term = p0.toString()

            }

            override fun afterTextChanged(p0: Editable?) {
                searchViewModel.obtainEvent(SearchEffect.ScreenShow(term = edit_text_search.text.toString()))
                  }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewModel.apply {
            viewStates().observe(viewLifecycleOwner, Observer { bindViewState(it) })
            viewEffects().observe(viewLifecycleOwner, Observer { bindViewAction(it) })
        }
        searchViewModel.obtainEvent(viewEvent = SearchEffect.ScreenShow(term = term))

        recycler_view_search.adapter = adapter
        recycler_view_search.layoutManager = GridLayoutManager(requireContext(), 3)

    }

    private fun bindViewState(viewState: SearchViewState) {
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

    private fun bindViewAction(viewAction: SearchAction) {
        when (viewAction) {
            is SearchAction.ShowError -> {
                Toast.makeText(
                    requireContext(),
                    viewAction.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}