package com.guowei.colorsapp.ui.colors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guowei.colorsapp.ui.common.utils.SingleLiveEvent
import com.guowei.colorsapp.ui.common.viewmodel.BaseViewModel
import com.guowei.colorsapp.usecase.ColorsUseCase
import com.guowei.colorsapp.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ColorsViewModel @Inject constructor(
    private val colorsUseCase: ColorsUseCase,
    private val userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private var _isLoggedInLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val isLoggedInLiveData: LiveData<Boolean> get() = _isLoggedInLiveData

    private var _uiModelLiveData: MutableLiveData<ColorsUiModel> = savedStateHandle.getLiveData(
        CURRENT_COLOR_LIVEDATA,
        ColorsUiModel(
            currentColorServer = null,
            currentColorLocal = null,
            colorSet = null,
            isLoading = false
        )
    )
    val uiModelLiveData: LiveData<ColorsUiModel> get() = _uiModelLiveData

    private var _logoutLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val logoutLiveData: LiveData<Boolean> get() = _logoutLiveData

    private var _errorLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    init {
        userUseCase.isLoggedIn()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { isLoggedIn -> _isLoggedInLiveData.value = isLoggedIn },

                // treat error as not logged in for now
                { _isLoggedInLiveData.value = false }
            ).addToDisposable()
    }

    fun init() {
        Single.zip(
            colorsUseCase.getOrCreate(),
            colorsUseCase.getColorSet(),
            ::Pair
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _uiModelLiveData.value = _uiModelLiveData.value?.copy(isLoading = true)
            }
            .doFinally {
                _uiModelLiveData.value = _uiModelLiveData.value?.copy(isLoading = false)
            }
            .subscribe(
                {
                    val currentUiModel = _uiModelLiveData.value
                    val newUiModel = if (currentUiModel?.currentColorLocal == null) {
                        ColorsUiModel(
                            currentColorServer = it.first,
                            currentColorLocal = it.first,
                            colorSet = it.second,
                            isLoading = false
                        )
                    } else {
                        currentUiModel.copy(
                            currentColorServer = it.first, colorSet = it.second, isLoading = false
                        )
                    }
                    _uiModelLiveData.value = newUiModel
                },
                {
                    _errorLiveData.value = "Failed loading current color!"
                }
            ).addToDisposable()
    }

    fun updateColor() {
        _uiModelLiveData.value?.currentColorLocal?.let {
            colorsUseCase.update(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    _uiModelLiveData.value = _uiModelLiveData.value?.copy(isLoading = true)
                }
                .doFinally {
                    _uiModelLiveData.value = _uiModelLiveData.value?.copy(isLoading = false)
                }
                .subscribe({ updatedColor ->
                    _uiModelLiveData.value = _uiModelLiveData.value!!.copy(
                        currentColorServer = updatedColor
                    )
                }, {
                    _errorLiveData.value = "Failed update color!"
                })
                .addToDisposable()
        }
    }

    fun previous() {
        _uiModelLiveData.value?.previous?.let {
            _uiModelLiveData.value = it
        }
    }

    fun next() {
        _uiModelLiveData.value?.next?.let {
            _uiModelLiveData.value = it
        }
    }

    fun logout() {
        userUseCase.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _uiModelLiveData.value = _uiModelLiveData.value?.copy(isLoading = true)
            }
            .doFinally {
                _uiModelLiveData.value = _uiModelLiveData.value?.copy(isLoading = false)
            }
            .subscribe({
                _logoutLiveData.value = true
            }, {
                _logoutLiveData.value = false

            }).addToDisposable()
    }

    companion object {
        private const val CURRENT_COLOR_LIVEDATA = "current_color"
    }
}
