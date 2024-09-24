package com.example.zapcar.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.data.local.CarRepository
import com.example.zapcar.R
import com.example.zapcar.data.CarsApi
import com.example.zapcar.domain.Carro
import com.example.zapcar.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {
    lateinit var fabcalcular: FloatingActionButton
    lateinit var listaCarros: RecyclerView
    lateinit var progress: ProgressBar
    lateinit var noInternetImage: ImageView
    lateinit var noIntentText: TextView
    lateinit var carsApi: CarsApi

    var carrosArray: ArrayList<Carro> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = inflater.inflate(R.layout.car_fragment, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupView(view)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            getAllcars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit() {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://lucasmrc81.github.io/cars-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllcars() {
        carsApi.getAllCars().enqueue(
            object : Callback<List<Carro>> {
                override fun onResponse(
                    call: Call<List<Carro>>,
                    response: Response<List<Carro>>,
                ) {
                    if (response.isSuccessful) {
                        progress.isVisible = false
                        noInternetImage.isVisible = false
                        noIntentText.isVisible = false

                        response.body()?.let {
                            setupList(it)
                        }
                    } else {
                        Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<Carro>>,
                    t: Throwable,
                ) {
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            },
        )
    }

    fun emptyState() {
        progress.isVisible = false
        listaCarros.isVisible = false
        noInternetImage.isVisible = true
        noIntentText.isVisible = true
    }

    fun setupView(view: View) {
        view.apply {
            fabcalcular = findViewById(R.id.fab_calcular)
            listaCarros = findViewById(R.id.rv_lista_caros)
            progress = findViewById(R.id.progress_bar)
            noInternetImage = findViewById(R.id.iv_empty_state)
            noIntentText = findViewById(R.id.tv_no_wifi)
        }
    }

    fun setupList(list: List<Carro>) {
        val carAdapter = CarAdapter(list)
        listaCarros.apply {
            isVisible = true
            adapter = carAdapter
        }
        carAdapter.carItemLister = { carro ->
            val isSaved = CarRepository(requireContext()).saveIfNotExist(Carro.id)
        }
    }

    fun setupListeners() {
        fabcalcular.setOnClickListener {
            startActivity(Intent(context, CalculaAutonomiaActivity::class.java))
        }
    }

    fun callService() {
        val urlBase = "https://raw.githubusercontent.com/Lucasmrc81/cars-api/be406fd0d97dffcdb34995c002f670eecfb8bedd/cars.json"
        progress.isVisible = true
        MyTask().execute(urlBase)
    }

    fun checkForInternet(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    inner class MyTask : AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando....")
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty(
                    "Accept",
                    "application/json",
                )

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                } else {
                    Log.e("Erro", "Serviço indisponivel no momento... ")
                }
            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao realizar processamento... ")
            } finally {
                urlConnection?.disconnect()
            }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray
                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("ID ->", id)

                    val marca = jsonArray.getJSONObject(i).getString("marca")
                    Log.d("Marca ->", marca)

                    val modelo = jsonArray.getJSONObject(i).getString("modelo")
                    Log.d("Modelo ->", modelo)

                    val precoNumericoString = jsonArray.getJSONObject(i).getString("precoNumerico")
                    val precoNumerico = precoNumericoString.toDoubleOrNull() ?: 0.0
                    Log.d("precoNumerico ->", precoNumerico.toString())

                    val precoString = jsonArray.getJSONObject(i).getString("preco")
                    val preco = precoString.toDoubleOrNull() ?: 0.0
                    Log.d("Preco ->", preco.toString())

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("bateria ->", bateria)

                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("potencia ->", potencia)

                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("recarga ->", recarga)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("urlPhoto ->", urlPhoto)

                    val model =
                        Carro(
                            id = id.toInt(),
                            marca = marca,
                            modelo = modelo,
                            precoNumerico = precoNumerico,
                            preco = preco.toString(),
                            bateria = bateria,
                            potencia = potencia,
                            recarga = recarga,
                            urlPhoto = urlPhoto,
                            isFavorite = false,
                        )
                    carrosArray.add(model)
                    Log.d("Model ->", model.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
