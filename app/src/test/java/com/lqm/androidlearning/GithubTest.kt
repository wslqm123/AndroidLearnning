package com.lqm.androidlearning

import com.lqm.androidlearning.data.GithubUser
import com.lqm.androidlearning.network.GithubApiService
import com.lqm.androidlearning.room.GithubRoomInterface
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GithubTest {
    // 选项 1: 手动创建 Mock
    val githubApi: GithubApiService = mock()
    val githubRoom: GithubRoomInterface = mock()

    // 被测系统 (SUT)
    val userManager = GithubUserManager(githubApi, githubRoom)

    @Test
    fun fetchUserName() {
        runBlocking {
            // Arrange (准备): 定义 Mock 对象的行为
            val expectedUser = GithubUser("Alice", 1)
            // 使用 mockito-kotlin :
            whenever(userManager.getUser("Alice")).thenReturn(expectedUser)
            // Act (行动): 调用被测方法
            val user = userManager.getUser("Alice")
            // Assert (断言): 检查结果
            assertEquals("Alice", user.login)
        }
    }

    @Test
    fun fetchUserName_ReturnsDefault() {
        runBlocking {
            // Arrange (准备): 为返回 null 进行打桩
            whenever(githubApi.getUser("")).thenReturn(null)
            // Act (行动)
            val userName = userManager.getUser("")
            // Assert (断言)
            assertEquals("Default User", userName.login)
        }

    }

    @Test
    fun saveUser() {
        runBlocking {
            // Arrange (准备) (这里不需要打桩，只需要观察交互)
            val user = GithubUser("Alice", 1)
            // Act (行动)
            userManager.saveUser(user)
            // Assert/Verify (断言/验证): 检查 saveUser 是否被调用
            verify(githubRoom).saveUser(eq(GithubUser("Alice", 1))) // 对于对象，使用匹配器时需要 eq()
            // 你也可以验证它被调用的次数
            verify(githubRoom, times(1)).saveUser(any())
        }
    }

    @Test
    fun fetchUserNameAction() {
        runBlocking {
            // Arrange (准备)
            whenever(githubApi.getUser("")).thenReturn(GithubUser("Generic", 0))

            // Act (行动)
            userManager.getUser("123")
            userManager.getUser("456")

            // Assert / Verify (断言 / 验证)
            // 检查 getUser 被调用了两次，参数是任意整数
            verify(githubApi, times(2)).getUser(any())

            // 检查它是否具体地被 123 调用过
            verify(githubApi).getUser(eq("123")) // 这里 eq() 是可选的，因为它是唯一的参数

            // 检查它是否具体地被 456 调用过
            verify(githubApi).getUser("456") // 如果没有使用其他匹配器，可以直接用原始值
        }

    }


}

class GithubUserManager(val api: GithubApiService, val room: GithubRoomInterface) {

    suspend fun getUser(name: String): GithubUser {
        return api.getUser(name) ?: GithubUser("Default User")
    }

    suspend fun saveUser(user: GithubUser) {
        room.saveUser(user)
    }
}