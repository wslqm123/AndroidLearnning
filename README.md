
## 📘 常用 GitHub API 列表（REST API v3）

---

### 👤 用户相关（Users）

| 功能             | 接口地址                                          | 示例调用                      |
|------------------|--------------------------------------------------|-------------------------------|
| 获取用户信息     | `GET /users/{username}`                          | `/users/octocat`             |
| 当前登录用户信息 | `GET /user`  （需要 OAuth）                     | `/user`                      |
| 用户的 followers | `GET /users/{username}/followers`                | `/users/octocat/followers`   |
| 用户关注的人     | `GET /users/{username}/following`                | `/users/octocat/following`   |

---

### 📦 仓库相关（Repositories）

| 功能                     | 接口地址                                                      | 示例调用                                |
|--------------------------|--------------------------------------------------------------|-----------------------------------------|
| 获取用户的仓库列表       | `GET /users/{username}/repos`                                | `/users/octocat/repos`                 |
| 获取仓库详细信息         | `GET /repos/{owner}/{repo}`                                  | `/repos/octocat/Hello-World`           |
| 获取仓库的 README        | `GET /repos/{owner}/{repo}/readme`                           | `/repos/octocat/Hello-World/readme`    |
| 获取仓库的 issues（公开）| `GET /repos/{owner}/{repo}/issues`                           | `/repos/octocat/Hello-World/issues`    |
| 获取仓库的 commits       | `GET /repos/{owner}/{repo}/commits`                          | `/repos/octocat/Hello-World/commits`   |

---

### 🔍 搜索相关（Search）

| 功能           | 接口地址                                 | 示例调用（带参数）                                                                 |
|----------------|-------------------------------------------|--------------------------------------------------------------------------------------|
| 搜索仓库       | `GET /search/repositories`               | `/search/repositories?q=android+language:kotlin&sort=stars`                        |
| 搜索用户       | `GET /search/users`                      | `/search/users?q=tom+repos:>10+followers:>100`                                     |
| 搜索 issues    | `GET /search/issues`                     | `/search/issues?q=repo:octocat/Hello-World+is:open+label:bug`                      |

---

### 🧭 公共活动相关（Events）

| 功能                   | 接口地址                           | 示例调用                                |
|------------------------|-------------------------------------|-----------------------------------------|
| 所有公开事件（推荐）  | `GET /events`                      | `/events`                               |
| 某用户的事件           | `GET /users/{username}/events`     | `/users/octocat/events`                 |
| 某仓库的事件           | `GET /repos/{owner}/{repo}/events`| `/repos/octocat/Hello-World/events`     |

---

### 🧪 推荐练手组合（练 Retrofit 超棒）：

| 目标                                         | 所用 API                                                  |
|----------------------------------------------|------------------------------------------------------------|
| 显示某用户的 GitHub 个人资料卡片              | `/users/{username}`                                       |
| 展示某用户的所有仓库（含 stars、描述）        | `/users/{username}/repos`                                 |
| 显示某仓库的最近提交历史                      | `/repos/{owner}/{repo}/commits`                           |
| 构建一个 GitHub 搜索 App（比如：搜索 Android）| `/search/repositories?q=android`                          |
| 做一个 Issues 浏览器                         | `/repos/{owner}/{repo}/issues`                            |

---

### 🔗 官方 API 文档（快速入口）

- **REST API 首页**：https://docs.github.com/en/rest
- **用户相关文档**：https://docs.github.com/en/rest/users/users
- **仓库相关文档**：https://docs.github.com/en/rest/repos/repos
- **搜索文档**：https://docs.github.com/en/rest/search
- **Events 事件流**：https://docs.github.com/en/rest/activity/events

---
