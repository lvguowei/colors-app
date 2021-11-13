package com.guowei.colorsapp.ui.colors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guowei.colorsapp.ui.common.utils.Consumable
import com.guowei.colorsapp.ui.common.utils.toConsumable
import com.guowei.colorsapp.ui.common.viewmodel.BaseViewModel
import com.guowei.colorsapp.usecase.ColorsUseCase
import com.guowei.colorsapp.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ColorsViewModel @Inject constructor(
    private val colorsUseCase: ColorsUseCase,
    private val userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private var _uiModelLiveData: MutableLiveData<ColorsUiModel> = savedStateHandle.getLiveData(
        CURRENT_COLOR_LIVEDATA,
        ColorsUiModel(
            currentColorServer = null,
            currentColorLocal = "#FFFFFF", // default to white
            colorSet = null,
            isLoading = false
        )
    )
    val uiModelLiveData: LiveData<ColorsUiModel> get() = _uiModelLiveData

    private var _logoutLiveData: MutableLiveData<Consumable<Boolean>> =
        savedStateHandle.getLiveData(LOGOUT_LIVEDATA)
    val logoutLiveData: LiveData<Consumable<Boolean>> get() = _logoutLiveData

    private var _errorLiveData: MutableLiveData<Consumable<String>> =
        savedStateHandle.getLiveData(ERROR_LIVEDATA)
    val errorLiveData: LiveData<Consumable<String>> get() = _errorLiveData

    init {
        Single.zip(
            colorsUseCase.getOrCreate(),
            colorsUseCase.getColorSet()
        ) { current: String, colorSet: List<String> ->
            ColorsUiModel(
                currentColorServer = current,
                currentColorLocal = current,
                colorSet = colorSet,
                isLoading = false
            )
        }
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
                    _uiModelLiveData.value = it
                },
                {
                    _errorLiveData.value = "Failed loading current color!".toConsumable()
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
                    _errorLiveData.value = "Failed update color!".toConsumable()
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
                _logoutLiveData.value = true.toConsumable()
            }, {
                _logoutLiveData.value = false.toConsumable()

            }).addToDisposable()
    }

    companion object {
        private const val CURRENT_COLOR_LIVEDATA = "current_color"
        private const val LOGOUT_LIVEDATA = "logout"
        private const val ERROR_LIVEDATA = "error"
    }
}