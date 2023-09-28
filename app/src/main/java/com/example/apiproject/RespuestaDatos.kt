package com.example.apiproject

data class RespuestaDatos (
    val name: String,
    val status: String,
    val species: String,
    val gender : String,
    val episode: List<String>,
    val image: String
)