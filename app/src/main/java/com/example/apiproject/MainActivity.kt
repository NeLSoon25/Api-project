package com.example.apiproject
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var rickAndMortyApi: ApiService
    private lateinit var selectorPersonaje: Spinner
    private lateinit var nombreTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var especieTextView: TextView
    private lateinit var episodesTextView: TextView
    private lateinit var searchButton: Button
    private lateinit var fotoImageView: ImageView
    private lateinit var genderTextView: TextView
    private lateinit var errorTextView: TextView

    // Lista de nombres de personajes para el Spinner
    private val characterNames = listOf(
        "Rick Sanchez",
        "Morty Smith",
        "Summer Smith",
        "Beth Smith",
        "Jessica",
        "Jerry Smith",
        "Mr. Poopybutthole",
        "Evil Morty",
        "Evil Rick",
        "Squanchy",
        "Talking Cat",
        "Birdperson",
        "Noob-Noob",
        "Mr. Meeseeks",
        "Scary Terry",
        "President Curtis",
        "Snuffles (Snowball)",
        "Doofus Rick"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nombreTextView = findViewById(R.id.textoNombre)
        selectorPersonaje = findViewById(R.id.characterSelector)
        statusTextView = findViewById(R.id.textoEstado)
        especieTextView = findViewById(R.id.textoEspecie)
        episodesTextView = findViewById(R.id.textoEpisodios)
        searchButton = findViewById(R.id.searchButton)
        fotoImageView = findViewById(R.id.imagenView)
        genderTextView = findViewById(R.id.textoGender)
        errorTextView = findViewById(R.id.textoError)

        //Retrofit
        retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        rickAndMortyApi = retrofit.create(ApiService::class.java)

        // Adapter para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, characterNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectorPersonaje.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, characterNames)

        searchButton.setOnClickListener {
            val character = selectorPersonaje.selectedItem.toString()
            val characterId = getCharacterIdByName(character)
            if (characterId != null) {
                getCharacter(characterId)
            }
        }
    }

    private fun getCharacterIdByName(characterName: String): Int? {

        when (characterName) {
            "Rick Sanchez" -> return 1
            "Morty Smith" -> return 2
            "Summer Smith" -> return 3
            "Jessica" -> return 180
            "Beth Smith" -> return 4
            "Jerry Smith" -> return 5
            "Mr. Poopybutthole" -> return 244
            "Evil Rick" -> return 119
            "Evil Morty" -> return 118
            "Squanchy" -> return 331
            "Talking Cat" -> return 564
            "Birdperson" -> return 47
            "Noob-Noob" -> return 252
            "Mr. Meeseeks" -> return 242
            "Scary Terry" -> return 306
            "President Curtis" -> return 347
            "Snuffles (Snowball)" -> return 329
            "Doofus Rick" -> return 103
            else -> return null
        }
    }

        private fun getCharacter(characterId: Int) {
        val call = rickAndMortyApi.getCharacter(characterId)

        call.enqueue(object : Callback<RespuestaDatos> {
            override fun onResponse(call: Call<RespuestaDatos>, response: Response<RespuestaDatos>) {
                if (response.isSuccessful) {
                    val character = response.body()
                    if (character != null) {
                        errorTextView.text = ""
                        nombreTextView.text = "Name: ${character.name}"
                        statusTextView.text = "Status: ${character.status}"
                        especieTextView.text = "Specie: ${character.species}"
                        genderTextView.text = "Gender: ${character.gender}"
                        episodesTextView.text = "Episodes: ${character.episode.size}"
                        Glide.with(this@MainActivity)
                            .load(character.image)
                            .override(500, 500)
                            .into(fotoImageView)
                    }
                }
            }

            override fun onFailure(call: Call<RespuestaDatos>, t: Throwable) {
                errorTextView.text = "No es posible conectarse a la API"
            }
        })
    }
}
