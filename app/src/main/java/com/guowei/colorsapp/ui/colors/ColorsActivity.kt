package com.guowei.colorsapp.ui.colors

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.ActivityColorsBinding
import com.guowei.colorsapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColorsActivity : AppCompatActivity() {

    private val viewModel: ColorsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(setContentView<ActivityColorsBinding>(this, R.layout.activity_colors)) {
            lifecycleOwner = this@ColorsActivity
            vm = viewModel
        }

        viewModel.isLoggedInLiveData.observe(this) {
            if (it) {
                viewModel.init()
            } else {
                LoginActivity.start(this@ColorsActivity)
                finish()
            }
        }

        viewModel.logoutLiveData.observe(this) {
            if (it) {
                LoginActivity.start(this@ColorsActivity)
                finish()
            } else {
                Toast.makeText(this@ColorsActivity, "logout failed", Toast.LENGTH_LONG).show()
            }
        }


        viewModel.errorLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ColorsActivity::class.java)
            context.startActivity(intent)
        }
    }
}