package com.example.mobifilatask.api.model


data class MainModel(val page: Int,  val perPage: Int,val total: Int,val totalPages: Int, val data: List<Data>, val support: Support)


