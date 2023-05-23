package com.home.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.fragment.BaseViewModelFragment
import com.home.R
import com.home.databinding.FragmentHomeBinding
import com.home.ui.adapter.MemeAdapter
import com.home.ui.viewmodel.HomeViewModel
import com.model.GoogleResponse
import com.service.endpoint.GoogleEndpoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class HomeFragment : BaseViewModelFragment<FragmentHomeBinding, HomeViewModel>() {

    override val bindingVariable: Int? = null
    override val getLayoutId: Int = R.layout.fragment_home
    override val viewmodel: HomeViewModel? by inject()

    override fun initialize() {
        super.initialize()
       // setupViewModel()
testRetrofit()
    }

    private fun testRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


         var  retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        val call: Call<GoogleResponse> = retrofit.create(APIInterface::class.java).getMemeImages()
        call.enqueue(object : Callback<GoogleResponse>{
            override fun onResponse(
                call: Call<GoogleResponse>,
                response: Response<GoogleResponse>
            ) {
                setupList(response.body()!!)
            }

            override fun onFailure(call: Call<GoogleResponse>, t: Throwable) {
              val ferrou = ""
            }

        })
    }

    private fun setupViewModel() {
        viewmodel?.initializer {}
        viewmodel?.getGoogleMemeImages("memes")
        viewmodel?.showLoading?.observe(viewLifecycleOwner) {
            if (it)
                binding?.memeProgress?.visibility = View.VISIBLE
            else
                binding?.memeProgress?.visibility = View.GONE
        }
        viewmodel?.uiModel?.observe(viewLifecycleOwner) {
           setupList(it)
        }
    }

    private fun setupList(param: GoogleResponse) {
binding?.memeList?.let {
    it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    it.adapter = MemeAdapter(param)
} } }


            interface APIInterface {

    @GET("customsearch/v1?key=AIzaSyAtmFkU4bRssmeZne8hl8Gybp7c6ucnL0E&cx=d555aa8bb6c474fd5&q=meme&searchType=image&lr=lang_pt&gl=br&cr=paísBR&hl=pt-BR&num100&start=12")
     fun getMemeImages() : Call<GoogleResponse>
}