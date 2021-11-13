package com.guowei.colorsapp.ui.splash

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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private var _isLoggedInLiveData: MutableLiveData<Consumable<Boolean>> =
        savedStateHandle.getLiveData(IS_LOGGED_IN_LIVEDATA)
    val isLoggedInLiveData: LiveData<Consumable<Boolean>> get() = _isLoggedInLiveData

    init {
        userUseCase.isLoggedIn()
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isLoggedIn -> _isLoggedInLiveData.value = isLoggedIn.toConsumable() },

                // treat error as not logged in for now
                { _isLoggedInLiveData.value = false.toConsumable() }
            ).addToDisposable()
    }

    companion object {
        private const val IS_LOGGED_IN_LIVEDATA = "is_logged_in"
    }
}