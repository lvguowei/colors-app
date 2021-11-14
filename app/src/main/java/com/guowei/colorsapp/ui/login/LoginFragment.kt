package com.guowei.colorsapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.loginClickedLiveData.observe(viewLifecycleOwner) {
            viewModel.login(
                binding.userNameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show()
            }
        }

        // Quit app if user press back button here
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

}