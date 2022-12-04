package com.common.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes

abstract class BaseActivity : CoreBaseActivity() {

    @LayoutRes
    open fun getLayoutId(): Int? = null

    override fun initialize(savedInstanceSTate: Bundle?) {
        getLayoutId()?.let { layoutId ->
            setContentView(layoutId)
        }
    }

}