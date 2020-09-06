package com.byfrunze.amazingwallpaper.network

import com.byfrunze.amazingwallpaper.data.Popular
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IRepositories {

    @GET("api2.0/get.php")
    fun getPopular(
        @Query("auth") auth: String,
        @Query("method") method: String = "popular",
        @Query("page") page: Int = 1
    ): Observable<Popular>

    @GET("api2.0/get.php")
    fun getSearch(
        @Query("auth") auth: String,
        @Query("method") method: String = "search",
        @Query("term") term: String,
        @Query("page") page: Int = 1
    ): Observable<Popular>

}