package com.example.poke_api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pokemonImageView: ImageView = itemView.findViewById(R.id.pokemonImageView)
        val pokemonNameTextView: TextView = itemView.findViewById(R.id.pokemonNameTextView)
        val pokemonAgeTextView: TextView = itemView.findViewById(R.id.pokemonAgeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.poke_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = pokemonList[position]
        holder.pokemonNameTextView.text = currentItem.name
        holder.pokemonAgeTextView.text = currentItem.age.toString()
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.pokemonImageView)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }
}
