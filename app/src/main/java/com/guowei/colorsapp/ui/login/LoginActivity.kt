package com.guowei.colorsapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.ActivityLoginBinding
import com.guowei.colorsapp.ui.colors.ColorsActivity
import com.guowei.colorsapp.ui.common.activity.BaseActivity
import com.guowei.colorsapp.ui.common.viewmodel.ViewModelFactory
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)

        binding = setContentView(this, R.layout.activity_login)

        with(binding) {
            lifecycleOwner = this@LoginActivity
            vm = viewModel
        }

        viewModel.loginClickedLiveData.observe(this, Observer {
            it.consume {
                viewModel.login(
                    binding.userNameEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        })

        viewModel.loginLiveData.observe(this, Observer {
            it.consume {
                if (this) {
                    ColorsActivity.start(this@LoginActivity)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}