package com.guowei.colorsapp.ui.colors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guowei.colorsapp.ui.common.utils.Consumable
import com.guowei.colorsapp.ui.common.utils.toConsumable
import com.guowei.colorsapp.ui.common.viewmodel.SavedStateViewModel
import com.guowei.colorsapp.usecase.ColorsUseCase
import com.guowei.colorsapp.usecase.UserUseCase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ColorsViewModel @Inject constructor(
    private val colorsUseCase: ColorsUseCase,
    private val userUseCase: UserUseCase
) : SavedStateViewModel() {

    private lateinit var _uiModelLiveData: MutableLiveData<ColorsUiModel>
    val uiModelLiveData: LiveData<ColorsUiModel> get() = _uiModelLiveData

    private lateinit var _logoutLiveData: MutableLiveData<Consumable<Boolean>>
    val logoutLiveData: LiveData<Consumable<Boolean>> get() = _logoutLiveData

    private lateinit var _errorLiveData: MutableLiveData<Consumable<String>>
    val errorLiveData: LiveData<Consumable<String>> get() = _errorLiveData

    override fun init(savedStateHandle: SavedStateHandle) {
        _uiModelLiveData = savedStateHandle.getLiveData(
            CURRENT_COLOR_LIVEDATA,
            ColorsUiModel(
                currentColorServer = null,
                currentColorLocal = "#FFFFFF", // default to white
                colorSet = null,
                isLoading = false
            )
        )
        _logoutLiveData = savedStateHandle.getLiveData(LOGOUT_LIVEDATA)
        _errorLiveData = savedStateHandle.getLiveData(ERROR_LIVEDATA)

        Single.zip(
            colorsUseCase.getOrCreate(),
            colorsUseCase.getColorSet(),
            BiFunction { current: String, colorSet: List<String> ->
                ColorsUiModel(
                    currentColorServer = current,
                    currentColorLocal = current,
                    colorSet = colorSet,
                    isLoading = false
                )
            })
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