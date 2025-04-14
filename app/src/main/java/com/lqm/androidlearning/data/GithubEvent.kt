package com.lqm.androidlearning.data

data class GithubEvent(
    val id: String? = null,
    val type: String? = null,
    val actor: GithubUser? = null,
) : BaseResponse

// {
//    "id": "48620721736",
//    "type": "PushEvent",
//    "actor": {
//      "id": 52763841,
//      "login": "Annysah",
//      "display_login": "Annysah",
//      "gravatar_id": "",
//      "url": "https://api.github.com/users/Annysah",
//      "avatar_url": "https://avatars.githubusercontent.com/u/52763841?"
//    },
//    "repo": {
//      "id": 426197775,
//      "name": "Annysah/Annysah",
//      "url": "https://api.github.com/repos/Annysah/Annysah"
//    },
//    "payload": {
//      "repository_id": 426197775,
//      "push_id": 23722426746,
//      "size": 1,
//      "distinct_size": 1,
//      "ref": "refs/heads/main",
//      "head": "495cff30a48eb5d10013adf6331845ac80eaf894",
//      "before": "4eb57119126e85709c136e9de105c980093abb83",
//      "commits": [
//        {
//          "sha": "495cff30a48eb5d10013adf6331845ac80eaf894",
//          "author": {
//            "email": "52763841+Annysah@users.noreply.github.com",
//            "name": "Anisat Ahmed"
//          },
//          "message": "Update README.md",
//          "distinct": true,
//          "url": "https://api.github.com/repos/Annysah/Annysah/commits/495cff30a48eb5d10013adf6331845ac80eaf894"
//        }
//      ]
//    },
//    "public": true,
//    "created_at": "2025-04-13T01:06:20Z"
//  }