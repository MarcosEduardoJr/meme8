package com.common.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.common.base.fragment.BaseFragment
import java.util.logging.Logger

fun <T : ViewDataBinding> BaseFragment.associateViewModel(
    layoutInflater: LayoutInflater,
    contanier: ViewGroup?,
    layoutId: Int? = null,
    viewModel: ViewModel? = null,
    bindingVariable: Int? = null
): T?{
    val binding = layoutId?.let {
        DataBindingUtil.inflate<T>(layoutInflater, it , contanier, false)
    }
    binding?.lifecycleOwner = viewLifecycleOwner
    bindingVariable?.let { bindingVariableLet ->
        binding?.setVariable(bindingVariableLet, viewModel)
    }

    binding?.executePendingBindings()

    return binding
}

fun Fragment.navigateTo(direction: NavDirections){
    try {
        findNavController().navigate(direction)
    }catch (e : IllegalArgumentException){
        Logger.getLogger(e.message)
    }
}

fun Fragment.pop(){
    try {
        findNavController().navigateUp()
    }catch (e : IllegalArgumentException){
        Logger.getLogger(e.message)
    }
}

fun Fragment.popBackStack(){
    try {
        findNavController().popBackStack()
    }catch (e : IllegalArgumentException){
        Logger.getLogger(e.message)
    }
}
