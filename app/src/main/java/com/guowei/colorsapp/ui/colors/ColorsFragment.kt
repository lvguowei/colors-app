package com.guowei.colorsapp.ui.colors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.guowei.colorsapp.R
import com.guowei.colorsapp.databinding.FragmentColorsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ColorsFragment : Fragment() {

    private lateinit var binding: FragmentColorsBinding

    private val viewModel: ColorsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_colors, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isLoggedInLiveData.observe(viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                viewModel.loadColors()
            } else {
                findNavController().navigate(R.id.action_colorsFragment_to_loginFragment)
            }
        }

        viewModel.logoutLiveData.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_colorsFragment_to_loginFragment)
            } else {
                Toast.makeText(context, "logout failed", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkLoggedIn()
    }
}