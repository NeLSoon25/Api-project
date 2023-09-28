package com.example.apiproject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("character/{id}")
    fun getCharacter(@Path("id") characterId: Int): Call<RespuestaDatos>
}