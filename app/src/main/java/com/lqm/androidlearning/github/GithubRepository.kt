package com.lqm.androidlearning.github

import com.lqm.androidlearning.network.NetworkUtils
import kotlinx.coroutines.flow.flow

class GithubRepository {

    suspend fun getUserFlow(username: String) = flow {
        val result = NetworkUtils.getDefaultApi().getUser(username)
        emit(result)
    }

    suspend fun getEventsFlow() = flow {
        val result = NetworkUtils.getDefaultApi().getEvent()
        emit(result)
    }
}