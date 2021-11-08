package com.guowei.colorsapp.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.ActivitySplashBinding
import com.guowei.colorsapp.ui.colors.ColorsActivity
import com.guowei.colorsapp.ui.common.activity.BaseActivity
import com.guowei.colorsapp.ui.common.viewmodel.ViewModelFactory
import com.guowei.colorsapp.ui.login.LoginActivity
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)) {
            lifecycleOwner = this@SplashActivity
        }

        injector.inject(this)

        viewModel.isLoggedInLiveData.observe(this, Observer {
            it.consume {
                if (this) {
                    ColorsActivity.start(this@SplashActivity)
                } else {
                    LoginActivity.start(this@SplashActivity)
                }
                finish()
            }
        })
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            context.startActivity(intent)
        }
    }
}