package com.jejakkarbon.data.network.api

import android.content.Context
import com.jejakkarbon.preferences.Preferences
import com.jejakkarbon.utils.common.Constant.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private lateinit var apiService: ApiService

    fun initialize(context: Context) {
        val preferences = Preferences(context)
        val userToken by lazy { preferences.getToken() }

        val client = OkHttpClient.Builder().apply {
            if (userToken.isLogin) {
                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${userToken.token}").build()
                    chain.proceed(request)
                }
            }
        }.build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getApiService(): ApiService {
        if (!ApiConfig::apiService.isInitialized) {
            throw IllegalStateException("ApiService is not initialized. Call initialize() first.")
        }
        return apiService
    }
}