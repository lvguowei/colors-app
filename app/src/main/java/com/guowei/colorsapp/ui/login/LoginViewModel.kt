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
) : BaseViewModel() {

    /**
     * True: login succeeded
     * False: login failed
     */
    private var _loginLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val loginLiveData: LiveData<Boolean> get() = _loginLiveData

    private var _loginClickedLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    val loginClickedLiveData: LiveData<Unit> get() = _loginClickedLiveData

    private var _loadingLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

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
                })
            .addToDisposable()
    }

    fun onLoginClicked() {
        _loginClickedLiveData.call()
    }
}