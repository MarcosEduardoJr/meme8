package com.home.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.common.base.viewmodel.BaseViewModel
import com.domain.usecase.google.GetGoogleMemeImagesUseCase
import com.model.GoogleResponse

class HomeViewModel constructor(
    private val getGoogleMemeImagesUseCase: GetGoogleMemeImagesUseCase
): BaseViewModel(){
    val uiModel: MutableLiveData<GoogleResponse> = MutableLiveData()
    val error : MutableLiveData<Boolean> = MutableLiveData()

    fun getGoogleMemeImages(query: String){
        getGoogleMemeImagesUseCase(query).singleExec(
            onSuccessBaseViewModel = {
                uiModel.value = it
            },
            onCompletionBaseViewModel = {
            showLoading.value = false
            },
            onError = { _, err, _ ->
                error.value = true
            }
        )
    }

}