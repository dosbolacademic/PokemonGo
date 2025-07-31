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

    private fun fetchDogImages() {
        val client = AsyncHttpClient()

        client["https://dog.ceo/api/breeds/image/random/20", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog", "response successful: $json")

//                val petImageURL = json.jsonObject.getString("message") // pet image URL set: https://images.dog.ceo/breeds/terrier-american/n02093428_4654.jpg
//                val petImageURLSplit = petImageURL.split("/");
//                val petBreed = petImageURLSplit[4]

                val petImageArray = json.jsonObject.getJSONArray("message")
                for (i in 0 until petImageArray.length()) {
                    petList.add(petImageArray.getString(i))
                }

//                Log.d("petImageURL", "pet image URL set: $petImageURL")
//                Log.d("petImageURLSplit", "pet image URL split into: $petImageURLSplit")
//                Log.d("petBreed", "pet breed set: $petBreed")

                val adapter = PetAdapter(petList)
                rvPets.adapter = adapter
                rvPets.layoutManager = LinearLayoutManager(this@MainActivity)

//                val imageView = findViewById<ImageView>(R.id.pet_image)
//                Glide.with(this@MainActivity)
//                    .load(petImageURL)
//                    .fitCenter()
//                    .into(imageView)
//
//                val textView = findViewById<TextView>(R.id.pet_breed)
//                textView.text = petBreed
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }
}