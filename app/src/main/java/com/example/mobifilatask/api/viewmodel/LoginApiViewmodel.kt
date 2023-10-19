package com.example.mobifilatask.api.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobifilatask.api.model.LoginRequest
import com.example.mobifilatask.api.myinterface.ApiCall
import com.example.mobifilatask.api.myinterface.RetrofitClient
import com.example.mobifilatask.api.model.MainModel
import com.example.mobifilatask.api.myinterface.GetRequestResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class LoginApiViewmodel : ViewModel() {
    lateinit var api: ApiCall
    var data = MutableLiveData<MainModel>()

    init {
        api = RetrofitClient.getRetrofitInstance().create(ApiCall::class.java)
    }

    fun getLoginResponse(loginreq: LoginRequest, getRequestResponse: GetRequestResponse) {
        try {


            val call = api.apiLogin(loginreq)
            call.enqueue(object : retrofit2.Callback<MainModel> {
                override fun onResponse(call: Call<MainModel>, response: Response<MainModel>) {
                    data.value = response.body()
                    getRequestResponse.onResponse(Gson().toJson(response.body()))
                }

                override fun onFailure(call: Call<MainModel>, t: Throwable) {
                    getRequestResponse.onError(t.toString())
                }

            })
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }
    }
}