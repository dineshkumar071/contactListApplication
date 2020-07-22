package com.intern.internproject.retrofit

import com.google.gson.GsonBuilder
import com.intern.internproject.respository.CLRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CLRetrofit {
    fun retrofit(): Retrofit? {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp = OkHttpClient.Builder().addInterceptor(logger)
        return Retrofit.Builder()
            .baseUrl(CLRepository.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttp.build())
            .build()
    }
}