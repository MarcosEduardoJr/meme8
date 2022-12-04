package com.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.common.extensions.associateViewModel
import com.common.extensions.pop

abstract class BaseViewModelFragment<T : ViewDataBinding, VM : ViewModel> : BaseFragment() {

    abstract val bindingVariable: Int?

    abstract val getLayoutId: Int?

    abstract val viewmodel: VM?

    var binding: T? = null

    override fun initialize() {
        activity?.onBackPressedDispatcher?.addCallback(this){
            onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = associateViewModel(
            inflater,
            container,
            getLayoutId,
            viewmodel,
            bindingVariable
        )
        return binding?.root ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun onBackPressed(){
        pop()
    }
}