package com.lqm.androidlearning.github

import com.lqm.androidlearning.network.NetworkUtils
import kotlinx.coroutines.flow.flow

class GithubRepository {

    suspend fun getUserFlow(username: String) = flow {
        val result = NetworkUtils.getGithubApi().getUser(username)
        emit(result)
    }

    suspend fun getEventsFlow() = flow {
        val result = NetworkUtils.getGithubApi().getEvent()
        emit(result)
    }
}