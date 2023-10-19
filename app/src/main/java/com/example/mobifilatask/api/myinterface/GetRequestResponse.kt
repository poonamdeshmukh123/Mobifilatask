package com.example.mobifilatask.api.myinterface

interface GetRequestResponse {
    fun onResponse(json: String)
    fun onError(json: String)
}