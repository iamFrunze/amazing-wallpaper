package com.byfrunze.amazingwallpaper.presentation.screens.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.byfrunze.amazingwallpaper.R
import com.byfrunze.amazingwallpaper.data.Wallpaper
import com.byfrunze.amazingwallpaper.presentation.helpers.Identify
import com.byfrunze.amazingwallpaper.presentation.helpers.Identify.*
import com.byfrunze.amazingwallpaper.presentation.helpers.injectViewModel
import com.byfrunze.amazingwallpaper.presentation.viewstate.AuthStatus
import com.byfrunze.amazingwallpaper.presentation.viewstate.MainAction
import com.byfrunze.amazingwallpaper.presentation.viewstate.SideEffect
import com.byfrunze.amazingwallpaper.presentation.viewstate.UserViewState
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : Fragment(R.layout.fragment_main) {

    var term = ""
    lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel

    val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = injectViewModel(factory = viewModeFactory)

        adapter.imagesAdapterClicks = object : ImagesAdapterClicks {
            override fun onItemClick(model: Wallpaper) {
                val bundle = Bundle()
                bundle.putString("url", model.url_image)
                findNavController().navigate(R.id.setupFragment, bundle)
            }
        }
        var page = 2

        adapter.loadImages = object : OnLoadImages {
            override fun inLoad(load: Boolean) {
                if (load) {
                    Log.i("TAG", "PAGE $page $term")
                    if (term.isEmpty())
                        mainViewModel.obtainEvent(
                            SideEffect.LoadMoreWallpaper(
                                page = page++,
                                term = ""
                            ), flag = POPULAR.flag
                        )
                    else mainViewModel.obtainEvent(
                        SideEffect.LoadMoreWallpaper(
                            page = page++,
                            term = term
                        ), flag = SEARCH.flag
                    )
                }

            }
        }
        edit_text_search_main.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.obtainEvent(
                    SideEffect.ScreenShow(
                        term = p0.toString()
                    ), flag = SEARCH.flag
                )
                term = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                mainViewModel.obtainEvent(
                    SideEffect.ScreenShow(
                        term = p0.toString()
                    ), flag = SEARCH.flag
                )
                term = p0.toString()
            }
        })


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.apply {
            viewStates().observe(viewLifecycleOwner, Observer { bindViewState(it) })
            viewEffects().observe(viewLifecycleOwner, Observer { bindViewAction(it) })
        }
        gridLayoutManager = GridLayoutManager(requireContext(), 3)

        mainViewModel.obtainEvent(viewEvent = SideEffect.ScreenShow(term = ""), flag = POPULAR.flag)
        recycler_view_images_main.adapter = adapter
        recycler_view_images_main.layoutManager = gridLayoutManager


    }

    private fun bindViewState(viewState: UserViewState) {
        when (viewState.authStatus) {
            is AuthStatus.Loading -> {
                recycler_view_images_main.visibility = View.GONE
                progress_bar_main.visibility = View.VISIBLE
            }
            is AuthStatus.Success -> {
                progress_bar_main.visibility = View.GONE
                recycler_view_images_main.visibility = View.VISIBLE
                viewState.data?.let {
                    adapter.initArray(it)
                }

            }
            is AuthStatus.AddMoreWallpaper -> {
                viewState.data?.let {
                    Log.i("SEARCH", it.toString())
                    adapter.addImages(it)
                }
            }
        }
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