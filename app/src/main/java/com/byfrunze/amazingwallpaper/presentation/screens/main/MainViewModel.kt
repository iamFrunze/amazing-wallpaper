package com.byfrunze.amazingwallpaper.presentation.screens.main

import android.util.Log
import com.byfrunze.amazingwallpaper.network.IRepositories
import com.byfrunze.amazingwallpaper.presentation.base.BaseViewModel
import com.byfrunze.amazingwallpaper.presentation.helpers.Identify
import com.byfrunze.amazingwallpaper.presentation.helpers.Identify.*
import com.byfrunze.amazingwallpaper.presentation.viewstate.AuthStatus
import com.byfrunze.amazingwallpaper.presentation.viewstate.MainAction
import com.byfrunze.amazingwallpaper.presentation.viewstate.SideEffect
import com.byfrunze.amazingwallpaper.presentation.viewstate.UserViewState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel
@Inject constructor() : BaseViewModel<UserViewState, MainAction, SideEffect>() {

    private val compositeDisposable = CompositeDisposable()

    init {
        viewState =
            UserViewState(authStatus = AuthStatus.Loading, data = emptyList(), flag = POPULAR.flag)

    }

    val API_KEY = "8304bf2e6183717d7caa61fe4ba68da2"

    @Inject
    lateinit var iRepositories: IRepositories

    private fun initialWallpaper(auth: String, term: String, flag: Int) {
        viewState = viewState.copy(authStatus = AuthStatus.Loading)
        when (flag) {
            POPULAR.flag -> {
                val popular =
                    iRepositories.getPopular(
                        auth = auth
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewState =
                                UserViewState(
                                    authStatus = AuthStatus.Success,
                                    data = it.wallpapers,
                                    flag = POPULAR.flag
                                )
                        }, {
                            viewAction = MainAction.ShowError(error = it.localizedMessage)

                        })

                compositeDisposable.addAll(popular)
            }
            SEARCH.flag -> {
                viewState =
                    viewState.copy(authStatus = AuthStatus.Loading, flag = SEARCH.flag)
                val disposable = iRepositories
                    .getSearch(auth = auth, term = term, page = 1)
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState = viewState.copy(authStatus = AuthStatus.Success, data = it.wallpapers)

                    }, {
                        viewAction = MainAction.ShowError(error = it.localizedMessage)
                        Log.i("SEARCH", "tthis" + it.localizedMessage)

                    })
                compositeDisposable.add(disposable)
            }
        }

    }

    private fun loadMoreWallpaper(auth: String, page: Int, term: String, flag: Int) {
        viewState = viewState.copy(authStatus = AuthStatus.Loading)
        when (flag) {
            POPULAR.flag -> {
                val popular =
                    iRepositories.getPopular(
                        auth = auth,
                        page = page
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewState = UserViewState(
                                authStatus = AuthStatus.AddMoreWallpaper,
                                data = it.wallpapers,
                                flag = POPULAR.flag
                            )
                        }, {
                            viewAction = MainAction.ShowError(error = it.localizedMessage)
                        })
                compositeDisposable.addAll(popular)
            }
            SEARCH.flag -> {
                viewState =
                    viewState.copy(authStatus = AuthStatus.AddMoreWallpaper, flag = SEARCH.flag)
                val disposable = iRepositories
                    .getSearch(auth = auth, term = term, page = page)
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState = viewState.copy(data = it.wallpapers)

                    }, {
                        viewAction = MainAction.ShowError(error = it.localizedMessage)

                    })
                compositeDisposable.add(disposable)
            }
        }
    }


    override fun obtainEvent(viewEvent: SideEffect, flag: Int) {
        when (viewEvent) {
            is SideEffect.ScreenShow -> {
                Log.i("SEARCH", "EVENT $flag")
                initialWallpaper(
                    auth = API_KEY,
                    term = viewEvent.term,
                    flag = flag
                )
            }
            is SideEffect.LoadMoreWallpaper -> {
                Log.i("TAG", "${viewEvent.page}")
                loadMoreWallpaper(
                    auth = API_KEY,
                    page = viewEvent.page,
                    term = viewEvent.term,
                    flag = viewState.flag
                )


            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}

