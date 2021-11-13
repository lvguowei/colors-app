package com.guowei.colorsapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guowei.colorsapp.ui.common.utils.SingleLiveEvent
import com.guowei.colorsapp.ui.common.viewmodel.BaseViewModel
import com.guowei.colorsapp.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private var _loginLiveData: MutableLiveData<Boolean> =
        savedStateHandle.getLiveData(LOGIN_LIVEDATA)
    val loginLiveData: LiveData<Boolean> get() = _loginLiveData

    private var _loadingLiveData: MutableLiveData<Boolean> =
        savedStateHandle.getLiveData(LOADING_LIVEDATA, false)
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private var _loginClickedLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    val loginClickedLiveData: LiveData<Unit> get() = _loginClickedLiveData

    fun login(username: String, password: String) {
        userUseCase.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loadingLiveData.value = true }
            .doFinally { _loadingLiveData.value = false }
            .subscribe(
                {
                    _loginLiveData.value = true
                },
                {
                    _loginLiveData.value = false
                }).addToDisposable()
    }

    fun onLoginClicked() {
        _loginClickedLiveData.call()
    }

    companion object {
        private const val LOGIN_LIVEDATA = "login"
        private const val LOADING_LIVEDATA = "loading"
    }
}