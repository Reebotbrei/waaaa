package com.example.semana8_web.apiservices

import retrofit2.create
object RetrofitClient {

    private const val BASE_URL="http://proyecto-moviles.atwebpages.com/api/"

    val api: ApiService by lazy {
        retrofit2.Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create()).
                build().
                create(ApiService::class.java)
    }
}