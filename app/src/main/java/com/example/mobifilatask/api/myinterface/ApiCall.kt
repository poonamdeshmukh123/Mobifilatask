package com.example.mobifilatask.api.myinterface

import com.example.mobifilatask.api.model.LoginRequest
import com.example.mobifilatask.api.model.MainModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiCall {
    @POST("api/login")
    fun apiLogin(@Body loginRequest: LoginRequest): Call<MainModel>
}