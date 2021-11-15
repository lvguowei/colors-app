package com.guowei.colorsapp.ui.login

import com.google.common.truth.Truth
import com.guowei.colorsapp.ui.ViewModelTest
import com.guowei.colorsapp.usecase.UserUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

class LoginViewModelTest : ViewModelTest() {

    @MockK
    lateinit var userUseCase: UserUseCase

    private lateinit var SUT: LoginViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        SUT = LoginViewModel(userUseCase)
    }

    @Test
    fun `login succeeded`() {
        every { userUseCase.login(any(), any()) } returns Completable.complete()
        SUT.login("test", "123")
        Truth.assertThat(SUT.loginLiveData.value).isTrue()
    }

    @Test
    fun `login failed`() {
        every { userUseCase.login(any(), any()) } returns Completable.error(Throwable())
        SUT.login("test", "123")
        Truth.assertThat(SUT.loginLiveData.value).isFalse()
    }

}