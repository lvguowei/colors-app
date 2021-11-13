package com.guowei.colorsapp.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.ActivitySplashBinding
import com.guowei.colorsapp.ui.colors.ColorsActivity
import com.guowei.colorsapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)) {
            lifecycleOwner = this@SplashActivity
        }

        viewModel.isLoggedInLiveData.observe(this) {
            it.consume {
                if (this) {
                    ColorsActivity.start(this@SplashActivity)
                } else {
                    LoginActivity.start(this@SplashActivity)
                }
                finish()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            context.startActivity(intent)
        }
    }
}