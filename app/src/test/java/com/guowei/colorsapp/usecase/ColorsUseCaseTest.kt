package com.guowei.colorsapp.usecase

import com.guowei.colorsapp.cache.SessionCache
import com.guowei.colorsapp.networking.api.StorageApi
import com.guowei.colorsapp.networking.schema.StorageResponse
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class ColorsUseCaseTest {
    private val storageApi = mockk<StorageApi>()
    private val sessionCache = mockk<SessionCache>()

    private lateinit var useCase: ColorsUseCase

    private val storageId = "1"
    private val color = "#000000"

    @Before
    fun setup() {
        RxJavaPlugins.reset()
        useCase = ColorsUseCase(storageApi, sessionCache)
    }

    @After
    fun tearDown() = RxJavaPlugins.reset()

    @Test
    fun getOrCreate_noStorageId_shouldCreate() {

        every { sessionCache.getStorageId() } returns null
        every { storageApi.create(any()) } returns Single.just(StorageResponse(storageId, color))
        every { sessionCache.saveStorageId(storageId) } just Runs

        useCase.getOrCreate().test().assertValue(color)

        verify {
            sessionCache.saveStorageId(storageId)
            storageApi.get(storageId) wasNot Called
        }
    }

    @Test
    fun getOrCreate_hasStorageId_shouldGet() {
        every { sessionCache.getStorageId() } returns storageId
        every { storageApi.get(storageId) } returns Single.just(
            StorageResponse(
                storageId,
                color
            )
        )


        useCase.getOrCreate().test().assertValue(color)

        verify {
            storageApi.get(storageId)
            storageApi.create(any()) wasNot Called
        }
    }

    @Test
    fun getOrCreate_error_succeed_on_3rd_retry() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        every { sessionCache.getStorageId() } returns storageId
        every { storageApi.get(storageId) } returnsMany listOf(
            Single.error(RuntimeException()),
            Single.error(RuntimeException()),
            Single.just(
                StorageResponse(
                    storageId,
                    color
                )
            )
        )

        val testObserver = useCase.getOrCreate().test()
        testScheduler.advanceTimeBy(3, TimeUnit.SECONDS)
        testObserver.assertComplete()

        verify(exactly = 3) {
            storageApi.get(storageId)
        }

        verify { storageApi.create(any()) wasNot Called }
    }

}