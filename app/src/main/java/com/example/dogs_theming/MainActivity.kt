package com.example.dogs_theming

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var petList: MutableList<String>
    private lateinit var rvPets: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvPets = findViewById<RecyclerView>(R.id.pet_list)
        petList = mutableListOf()
        fetchDogImages()
    }

    private fun fetchDogImages() {  // keep method name same for now
        val client = AsyncHttpClient()

        val pokemonListUrl = "https://pokeapi.co/api/v2/pokemon?limit=20"

        client[pokemonListUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Headers,
                json: JsonHttpResponseHandler.JSON
            ) {
                val results = json.jsonObject.getJSONArray("results")
                // Temporary list to hold the URLs for detailed calls
                val detailUrls = mutableListOf<String>()
                for (i in 0 until results.length()) {
                    val obj = results.getJSONObject(i)
                    detailUrls.add(obj.getString("url"))
                }

                // Now fetch each pokemon detail to get image and name
                for (url in detailUrls) {
                    client[url, object : JsonHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Headers,
                            jsonDetail: JsonHttpResponseHandler.JSON
                        ) {
                            val name = jsonDetail.jsonObject.getString("name")
                            val sprites = jsonDetail.jsonObject.getJSONObject("sprites")
                            val imageUrl =
                                sprites.getString("front_default") // Pokémon sprite image

                            if (imageUrl != "null") {
                                petList.add("$name|$imageUrl")

                                // Notify adapter for each addition (optional: batch after all loaded)
                                runOnUiThread {
                                    rvPets.adapter?.notifyDataSetChanged()
                                }
                            }
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            errorResponse: String,
                            throwable: Throwable?
                        ) {
                            Log.d("Pokemon Detail Error", errorResponse)
                        }
                    }]
                }

                // Set adapter and layout manager once (it will update as items added)
                val adapter = PetAdapter(petList)
                rvPets.adapter = adapter
                rvPets.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon List Error", errorResponse)
            }
        }]
    }
}


