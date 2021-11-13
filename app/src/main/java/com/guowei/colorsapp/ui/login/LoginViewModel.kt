package com.guowei.colorsapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guowei.colorsapp.ui.common.utils.Consumable
import com.guowei.colorsapp.ui.common.utils.toConsumable
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

    private var _loginLiveData: MutableLiveData<Consumable<Boolean>> =
        savedStateHandle.getLiveData(LOGIN_LIVEDATA)
    val loginLiveData: LiveData<Consumable<Boolean>> get() = _loginLiveData

    private var _loadingLiveData: MutableLiveData<Boolean> =
        savedStateHandle.getLiveData(LOADING_LIVEDATA, false)
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private var _loginClickedLiveData: MutableLiveData<Consumable<Unit>> =
        savedStateHandle.getLiveData(LOGIN_CLICKED)
    val loginClickedLiveData: LiveData<Consumable<Unit>> get() = _loginClickedLiveData

    fun login(username: String, password: String) {
        userUseCase.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loadingLiveData.value = true }
            .doFinally { _loadingLiveData.value = false }
            .subscribe(
                {
                    _loginLiveData.value = true.toConsumable()
                },
                {
                    _loginLiveData.value = false.toConsumable()
                }).addToDisposable()
    }

    fun onLoginClicked() {
        _loginClickedLiveData.value = Unit.toConsumable()
    }

    companion object {
        private const val LOGIN_LIVEDATA = "login"
        private const val LOADING_LIVEDATA = "loading"
        private const val LOGIN_CLICKED = "login_clicked"
    }
}