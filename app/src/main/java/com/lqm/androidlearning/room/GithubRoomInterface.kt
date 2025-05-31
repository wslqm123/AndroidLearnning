package com.lqm.androidlearning.room

import com.lqm.androidlearning.data.GithubUser

interface GithubRoomInterface {

    suspend fun saveUser(user: GithubUser)
}