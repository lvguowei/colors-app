package com.guowei.colorsapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.ActivityLoginBinding
import com.guowei.colorsapp.ui.colors.ColorsActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView<ActivityLoginBinding?>(this, R.layout.activity_login).apply {
            lifecycleOwner = this@LoginActivity
            vm = viewModel
        }

        viewModel.loginClickedLiveData.observe(this) {
            it.consume {
                viewModel.login(
                    binding.userNameEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }

        viewModel.loginLiveData.observe(this) {
            it.consume {
                if (this) {
                    ColorsActivity.start(this@LoginActivity)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}