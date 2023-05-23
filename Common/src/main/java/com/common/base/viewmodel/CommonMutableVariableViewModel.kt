package com.common.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class CommonMutableVariableViewModel : ViewModel() {
    open val message = MutableLiveData<String>()
    open val showLoading = MutableLiveData<Boolean>().apply {
        value = false
    }
    open val makeLogout: MutableLiveData<String> = MutableLiveData()
}