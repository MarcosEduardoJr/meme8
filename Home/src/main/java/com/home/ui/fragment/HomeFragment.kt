package com.home.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.fragment.BaseViewModelFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.home.R
import com.home.databinding.FragmentHomeBinding
import com.home.ui.adapter.MemeAdapter
import com.home.ui.fragmentimport.saveImageToFirebaseStorage
import com.home.ui.viewmodel.HomeViewModel
import com.model.GoogleResponse
import com.model.gimme.Meme
import com.model.nine_gag.NineGagResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

const val QUANTITY_MEME_PEER_PAGE = 12

class HomeFragment : BaseViewModelFragment<FragmentHomeBinding, HomeViewModel>() {
    private lateinit var storage: FirebaseStorage
    private var loteAtual =  1
    private lateinit var layoutManager: LinearLayoutManager
    private var isloading = false
    private lateinit var adapter: MemeAdapter
    var startPageCount = QUANTITY_MEME_PEER_PAGE
    override val bindingVariable: Int? = null
    override val getLayoutId: Int = R.layout.fragment_home
    override val viewmodel: HomeViewModel? by inject()
    private var currentItemPosition = 0
    val nine_path = "v1/interest-posts/interest/memes/type/hot?"
    private var nextCursor = nine_path
    private var count = 10
    var repeat = 3000
    var currenItemUrl = ""
    // Referência para o Firebase Storage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storage = FirebaseStorage.getInstance()

        obterMidiasEmLotes(
            loteAtual,
            onFailure = {},
            onSuccess = { urls, temMais ->
                setupList(urls.toMutableList())
            })
        binding?.next?.setOnClickListener {
            val nextPosition = layoutManager.findFirstVisibleItemPosition() + 1
            binding?.memeList?.smoothScrollToPosition(nextPosition)
        }
        binding?.share?.setOnClickListener {
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            val requestCode = 1

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permissão já concedida, pode continuar com o compartilhamento do arquivo
                compartilharMidia(currenItemUrl, requireContext())
            } else {
                // Solicitar permissão ao usuário
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }
    override fun initialize() {
        super.initialize()
        // setupViewModel()

        FirebaseApp.initializeApp(requireContext())

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permissão concedida, pode continuar com o compartilhamento do arquivo
            compartilharMidia(currenItemUrl, requireContext())
        } else {
            // Permissão negada, lide com isso de acordo com a lógica do seu aplicativo
        }
    }

    fun generateUUID(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun saveLastSearch(lastSearch: String) {
        val sharedPreferences = context?.getSharedPreferences("lastSearch", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("lastSearch", lastSearch)
        editor?.apply()
    }

    fun getLastSearch(): String {
        val sharedPreferences =
            context?.getSharedPreferences("lastSearch", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("lastSearch", "").orEmpty()

    }

    private fun testRetrofit() {
        val lastSearch = getLastSearch()
        if (lastSearch.isNotEmpty())
            nextCursor = lastSearch
        isloading = true
        // binding?.memeProgress?.visibility = View.VISIBLE
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


        var retrofit = Retrofit.Builder()
            .baseUrl("https://9gag.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val call: Call<NineGagResponse> =
            retrofit.create(APIInterface::class.java).getNineGagMemes(nextCursor)
        call.enqueue(object : Callback<NineGagResponse> {
            override fun onResponse(
                call: Call<NineGagResponse>,
                response: Response<NineGagResponse>
            ) {
                if (response.isSuccessful) {
                    saveLastSearch(nine_path + response.body()?.data?.nextCursor.orEmpty())
                    startPageCount += QUANTITY_MEME_PEER_PAGE
                    count += QUANTITY_MEME_PEER_PAGE
                    for (post in response.body()?.data?.posts.orEmpty()) {
                        if (post.images.image460sv != null) {
                            post.images.image460sv?.let {
                                val scp = scope.launch {
                                    saveImageToFirebaseStorage(it.url, generateUUID())
                                }
                                scp.cancel()
                            }
                        } else {
                            val scp = scope.launch {
                                saveImageToFirebaseStorage(post.images.image460.url, generateUUID())
                            }
                            scp.cancel()
                        }
                    }
                    /* response.body()?.let {
                         setupList(it)
                     }*/
                    isloading = false
                    // binding?.memeProgress?.visibility = View.GONE
                    if (repeat > 0) {
                        repeat--
                        //  testRetrofit()
                    }
                } else
                    testRetrofit()
            }

            override fun onFailure(call: Call<NineGagResponse>, t: Throwable) {
                // testRetrofit()
            }

        })
    }

    /*    private fun testRetrofit() {
          isloading = true
          binding?.memeProgress?.visibility = View.VISIBLE
          val interceptor = HttpLoggingInterceptor()
          interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
          val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


          var retrofit = Retrofit.Builder()
              .baseUrl("https://meme-api.com/")
              .addConverterFactory(GsonConverterFactory.create())
              .client(client)
              .build()


          val call: Call<Meme> = retrofit.create(APIInterface::class.java).getMemes(15)
          call.enqueue(object : Callback<Meme> {
              override fun onResponse(
                  call: Call<Meme>,
                  response: Response<Meme>
              ) {
                  if (response.isSuccessful) {
                      startPageCount += QUANTITY_MEME_PEER_PAGE
                      response.body()?.let {
                          setupList(it)
                      }
                      isloading = false
                      binding?.memeProgress?.visibility = View.GONE
                  } else
                      testRetrofit()
              }

              override fun onFailure(call: Call<Meme>, t: Throwable) {
                  testRetrofit()
              }

          })
      }*/
    /*   private fun testRetrofit() {
             isloading = true
             binding?.memeProgress?.visibility = View.VISIBLE
             val interceptor = HttpLoggingInterceptor()
             interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
             val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


             var retrofit = Retrofit.Builder()
                 .baseUrl("https://www.googleapis.com/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .client(client)
                 .build()


             val call: Call<GoogleResponse> = retrofit.create(APIInterface::class.java).getMemeImages(startPageCount,"meme memedroid")
             call.enqueue(object : Callback<GoogleResponse> {
                 override fun onResponse(
                     call: Call<GoogleResponse>,
                     response: Response<GoogleResponse>
                 ) {
                     startPageCount += QUANTITY_MEME_PEER_PAGE
                     response.body()?.let {
                     setupList(it)
                     }
                     isloading = false
                     binding?.memeProgress?.visibility = View.GONE
                 }

                 override fun onFailure(call: Call<GoogleResponse>, t: Throwable) {
                     val ferrou = ""
                 }

             })
         }
 */
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
            //   setupList(it)
        }
    }

    private fun setupList(param: MutableList<String>) {
        if (!this::adapter.isInitialized) {
            binding?.memeList?.let {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                it.layoutManager = layoutManager
                val list = param ?: mutableListOf()
                adapter = MemeAdapter(
                    list,
                    currentItem = { currenItemUrl -> this.currenItemUrl = currenItemUrl })
                it.adapter = adapter
                it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val layoutManager =
                            LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                        val totalItemCount = layoutManager.itemCount
                        val lastVisible = layoutManager.findLastVisibleItemPosition()
                        val endHasBeenReached = lastVisible + 5 >= totalItemCount
                        if (totalItemCount > 0 && endHasBeenReached) {
                            //you have reached to the bottom of your recycler view
                            if (!isloading)
                                obterMidiasEmLotes(
                                    loteAtual,
                                    onFailure = {},
                                    onSuccess = { urls, temMais ->
                                        adapter.submitList(urls.toMutableList())
                                    })
                        }
                    }
                })
                it.isNestedScrollingEnabled = false
            }
        } else {
            adapter.submitList(param)
        }
    }

    companion object {

        // Defina a quantidade de mídias a serem recuperadas por vez
        private const val quantidadePorLote = 10
    }



    // Função para recuperar as mídias em lotes
    private fun obterMidiasEmLotes(
        loteAtual: Int,
        onSuccess: (List<String>, Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        isloading = true
        // Obter a referência para o storage raiz
        val storageRef = storage.reference

        // Definir a página inicial para o lote atual
            val paginaInicial = loteAtual

        // Obter a lista de arquivos no storage raiz
        storageRef.listAll()
            .addOnSuccessListener { listaArquivos ->


                // Calcular o índice do último arquivo a ser recuperado neste lote
                val indiceUltimoArquivo = paginaInicial + quantidadePorLote
                this.loteAtual = indiceUltimoArquivo
                // Verificar se existem mais arquivos para recuperar neste lote
                val existemMaisArquivos = indiceUltimoArquivo < listaArquivos.items.size

                // Obter os arquivos dentro do intervalo definido para este lote
                val arquivosLote = listaArquivos.items.subList(paginaInicial, indiceUltimoArquivo)

                // Recuperar as URLs das mídias neste lote
                val urlsMidias = mutableListOf<String>()
                arquivosLote.forEachIndexed { index, arquivo ->
                    arquivo.metadata
                        .addOnSuccessListener { metadata ->
                                arquivo.downloadUrl.addOnSuccessListener { url ->
                                    urlsMidias.add(url.toString().plus("."+metadata.contentType!!.split("/")[1].orEmpty()))
                                    // Verificar se todas as URLs foram recuperadas
                                    if (urlsMidias.size == arquivosLote.size) {
                                        isloading = false
                                        onSuccess(urlsMidias, existemMaisArquivos)
                                    }
                                }
                                    .addOnFailureListener { exception ->
                                        onFailure(exception)
                                    }
                            // Verificar se todas as URLs foram recuperadas

                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}


interface APIInterface {

    @GET("customsearch/v1?key=AIzaSyAtmFkU4bRssmeZne8hl8Gybp7c6ucnL0E&cx=d555aa8bb6c474fd5&searchType=image&num100")
    fun getMemeImages(
        @Query("start") startPageCount: Int,
        @Query("q") search: String
    ): Call<GoogleResponse>

    @GET("gimme/{count}")
    fun getMemes(
        @Path("count") count: Int
    ): Call<Meme>

    @GET("{path}")
    fun getNineGagMemes(
        @Path("path") path: String
    ): Call<NineGagResponse>
}