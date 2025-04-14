package com.lqm.androidlearning.network

import com.lqm.androidlearning.data.GithubEvent
import com.lqm.androidlearning.data.GithubUser
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GithubUser


    @GET("events")
    suspend fun getEvent(): List<GithubEvent>
}

