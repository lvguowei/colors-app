package com.guowei.colorsapp.usecase

import com.guowei.colorsapp.cache.SessionCache
import com.guowei.colorsapp.networking.api.StorageApi
import com.guowei.colorsapp.networking.api.UserApi
import com.guowei.colorsapp.networking.schema.LoginResponse
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class UserUseCaseTest {
    private val userApi = mockk<UserApi>()
    private val storageApi = mockk<StorageApi>()
    private val sessionCache = mockk<SessionCache>()

    private lateinit var useCase: UserUseCase

    private val username = "test"
    private val password = "123"
    private val token = "abc"
    private val storageId = "1"

    @Before
    fun setup() {
        RxJavaPlugins.reset()
        useCase = UserUseCase(userApi, storageApi, sessionCache)
    }

    @After
    fun tearDown() = RxJavaPlugins.reset()

    @Test
    fun isLoggedIn_noCachedToken_returnFalse() {
        every { sessionCache.getToken() }.returns(null)
        useCase.isLoggedIn().test().assertValue(false)
    }

    @Test
    fun isLoggedIn_hasCachedToken_returnTrue() {
        every { sessionCache.getToken() }.returns(token)
        useCase.isLoggedIn().test().assertValue(true)
    }

    @Test
    fun login_success() {
        every { userApi.login(any()) }.returns(Single.just(LoginResponse(token)))
        every { sessionCache.saveToken(any()) } just Runs
        useCase.login(username, password).test().assertComplete()

        verify {
            sessionCache.saveToken(token)
        }
    }

    @Test
    fun logout_success() {
        every { sessionCache.getStorageId() }.returns(storageId)
        every { sessionCache.clear() } just Runs
        every { storageApi.delete(storageId) }.returns(Completable.complete())

        useCase.logout().test().assertComplete()

        verify {
            storageApi.delete(storageId)
            sessionCache.clear()
        }
    }

    @Test
    fun logout_error_succeed_on_3rd_retry() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        every { sessionCache.getStorageId() }.returns(storageId)
        every { sessionCache.clear() } just Runs
        every { storageApi.delete(storageId) } returnsMany listOf(
            Completable.error(Throwable()),
            Completable.error(Throwable()),
            Completable.complete()
        )

        val testObserver = useCase.logout().test()
        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)
        testObserver.assertComplete()

        verify {
            sessionCache.clear()
        }

        verify(exactly = 3) {
            storageApi.delete(storageId)
        }
    }
}