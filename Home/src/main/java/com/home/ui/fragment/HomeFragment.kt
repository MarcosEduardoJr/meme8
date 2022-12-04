package com.home.ui.fragment

import com.common.base.fragment.BaseViewModelFragment
import com.common.base.viewmodel.BaseViewModel
import com.home.R
import com.home.databinding.FragmentHomeBinding

class HomeFragment : BaseViewModelFragment<FragmentHomeBinding, BaseViewModel>() {

    override val bindingVariable: Int? = null
    override val getLayoutId: Int = R.layout.fragment_home
    override val viewmodel: BaseViewModel? = null

    override fun initialize() {
        super.initialize()


    }


}