package com.splash.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.common.base.fragment.BaseViewModelFragment
import com.google.firebase.FirebaseApp
import com.home.ui.activity.HomeActivity
import com.splash.BR
import com.splash.R
import com.splash.databinding.FragmentSplashBinding
import com.splash.ui.viewmodel.SplashViewModel
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.concurrent.schedule

class SplashFragment : BaseViewModelFragment<FragmentSplashBinding, SplashViewModel>() {

    override val bindingVariable: Int = BR.splashViewModel
    override val getLayoutId: Int = R.layout.fragment_splash
    override val viewmodel: SplashViewModel? by inject()

    override fun initialize() {
        super.initialize()

        //FirebaseApp.initializeApp(requireContext())
        Timer().schedule(3000){
            startActivity(Intent(requireContext(),HomeActivity::class.java))
   //      navigateTo()
        }
    }
}