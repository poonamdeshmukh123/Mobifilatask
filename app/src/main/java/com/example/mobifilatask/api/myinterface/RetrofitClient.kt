package com.example.mobifilatask.api.myinterface

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient
{
    companion object
    {
        val baseurl:String="https://reqres.in/"
        fun getRetrofitInstance():Retrofit
        {
return Retrofit.Builder()
    .baseUrl(baseurl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
        }
    }
}