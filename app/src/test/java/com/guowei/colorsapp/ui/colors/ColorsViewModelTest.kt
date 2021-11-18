package com.guowei.colorsapp.ui.colors

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth
import com.guowei.colorsapp.ui.ViewModelTest
import com.guowei.colorsapp.ui.colors.ColorsViewModel.Companion.CURRENT_COLOR_LIVEDATA
import com.guowei.colorsapp.usecase.ColorsUseCase
import com.guowei.colorsapp.usecase.UserUseCase
import com.guowei.colorsapp.utils.getOrAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class ColorsViewModelTest : ViewModelTest() {

    @MockK
    lateinit var userUseCase: UserUseCase

    @MockK
    lateinit var colorsUseCase: ColorsUseCase

    private lateinit var SUT: ColorsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `check login test - logged in`() {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        SUT = ColorsViewModel(colorsUseCase, userUseCase, savedStateHandle)
        every { userUseCase.isLoggedIn() } returns Single.just(true)
        SUT.checkLoggedIn()
        Truth.assertThat(SUT.isLoggedInLiveData.getOrAwaitValue()).isTrue()
    }


    @Test
    fun `check login test - not logged in`() {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        SUT = ColorsViewModel(colorsUseCase, userUseCase, savedStateHandle)
        every { userUseCase.isLoggedIn() } returns Single.just(false)
        SUT.checkLoggedIn()
        Truth.assertThat(SUT.isLoggedInLiveData.getOrAwaitValue()).isFalse()
    }

    @Test
    fun `check login test - error`() {
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        SUT = ColorsViewModel(colorsUseCase, userUseCase, savedStateHandle)
        every { userUseCase.isLoggedIn() } returns Single.error(RuntimeException())
        SUT.checkLoggedIn()
        Truth.assertThat(SUT.isLoggedInLiveData.getOrAwaitValue()).isFalse()
    }

    @Test
    fun `init test - no restored data from savedStateHandle`() {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.set(CURRENT_COLOR_LIVEDATA, ColorsUiModel.Empty)

        SUT = ColorsViewModel(colorsUseCase, userUseCase, savedStateHandle)
        val testColorSet = listOf(
            "color1",
            "color2",
            "color3"
        )

        every { colorsUseCase.getOrCreate() } returns Single.just("color1")
        every { colorsUseCase.getColorSet() } returns Single.just(
            testColorSet
        )

        SUT.init()

        Truth.assertThat(SUT.uiModelLiveData.getOrAwaitValue())
            .isEqualTo(ColorsUiModel("color1", "color1", testColorSet))
    }

    @Test
    fun `init test - has restored data from savedStateHandle`() {
        val testColorSet = listOf(
            "color1",
            "color2",
            "color3"
        )

        val restoredColorsUiModel = ColorsUiModel(
            currentColorServer = "color1",
            currentColorLocal = "color2",
            colorSet = testColorSet
        )

        val savedStateHandle = SavedStateHandle()
        savedStateHandle.set(CURRENT_COLOR_LIVEDATA, restoredColorsUiModel)

        SUT = ColorsViewModel(colorsUseCase, userUseCase, savedStateHandle)

        SUT.init()

        verify { colorsUseCase.getOrCreate() wasNot Called }
        verify { colorsUseCase.getColorSet() wasNot Called }

        Truth.assertThat(SUT.uiModelLiveData.getOrAwaitValue())
            .isEqualTo(restoredColorsUiModel)
    }
}