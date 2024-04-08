package com.example.poke_api

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Headers
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    private val pokeList = mutableListOf<Pokemon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.pink) // Make sure you define pink color in your resources
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        pokemonAdapter = PokemonAdapter(pokeList)
        recyclerView.adapter = pokemonAdapter



        getPokemonList()
    }

    private fun getPokemonList() {
        val client = AsyncHttpClient()
        client["https://pokeapi.co/api/v2/pokemon?limit=100", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val results = json.jsonObject.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val pokemonObject = results.getJSONObject(i)
                        val name = pokemonObject.getString("name")
                        val url = pokemonObject.getString("url")
                        getNextPokemonDetails(url, name)
                    }
                } catch (e: JSONException) {
                    Log.e("MainActivity", "JSON Exception: ${e.localizedMessage}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("MainActivity", "Failed to fetch Pokémon list")
            }
        }]
    }

    private fun getNextPokemonDetails(url: String, name: String) {
        val client = AsyncHttpClient()
        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val sprites = json.jsonObject.getJSONObject("sprites")
                    val imageUrl = sprites.getString("front_default")
                    val id = json.jsonObject.getInt("id")
                    pokeList.add(Pokemon(name, id, imageUrl))
                    pokemonAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    Log.e("MainActivity", "JSON Exception: ${e.localizedMessage}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("MainActivity", "Failed to fetch Pokémon details")
            }
        }]
    }
}
