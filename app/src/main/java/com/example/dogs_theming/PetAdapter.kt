



package com.example.dogs_theming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PetAdapter (private val petList: List<String>) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petImage: ImageView
        val petBreed: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            petImage = view.findViewById(R.id.pet_image)
            petBreed = view.findViewById(R.id.pet_breed)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pet_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val petData = petList[position]  // format: "name|imageUrl"
        val splitData = petData.split("|")
        val petBreed = splitData[0]      // Pokémon name
        val petImageURL = splitData[1]   // Pokémon image url

        Glide.with(holder.itemView)
            .load(petImageURL)
            .centerCrop()
            .into(holder.petImage)

        holder.petBreed.text = petBreed
    }


    override fun getItemCount() = petList.size
}
