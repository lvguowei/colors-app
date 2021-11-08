package com.guowei.colorsapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guowei.colorsapp.ui.common.utils.Consumable
import com.guowei.colorsapp.ui.common.utils.toConsumable
import com.guowei.colorsapp.ui.common.viewmodel.SavedStateViewModel
import com.guowei.colorsapp.usecase.UserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : SavedStateViewModel() {

    private lateinit var _isLoggedInLiveData: MutableLiveData<Consumable<Boolean>>
    val isLoggedInLiveData: LiveData<Consumable<Boolean>> get() = _isLoggedInLiveData

    override fun init(savedStateHandle: SavedStateHandle) {
        _isLoggedInLiveData = savedStateHandle.getLiveData(IS_LOGGED_IN_LIVEDATA)

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