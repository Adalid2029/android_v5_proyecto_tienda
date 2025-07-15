package com.version5.tiendavirtual.data.red

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitCliente {
    private const val URL_BASE = "https://api.tienda.hostrend.net/api/v1/"
    private val interceptorLogging = HttpLoggingInterceptor().apply{
        level = HttpLoggingInterceptor.Level.BODY
}

    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(interceptorLogging)
        .addInterceptor { chain ->
            val requestOriginal = chain.request()
            val request = requestOriginal.newBuilder()
                .addHeader("ContentType", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "TiendaVirtual-Android/1.0")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .client(clienteHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiServicio: ApiServicio = retrofit.create(ApiServicio::class.java)
}