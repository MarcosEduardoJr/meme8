package com.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.home.databinding.ItemMemeBinding
import com.model.GoogleResponse
import com.model.Item

class MemeAdapter(private var memes: GoogleResponse)
    :RecyclerView.Adapter<MemeAdapter.MemeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
      val binding = ItemMemeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return MemeViewHolder(binding, parent.context)
    }

    class MemeViewHolder(private val binding: ItemMemeBinding,private val context: Context)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(googleResponse: Item?) {
                Glide.with(context).load(googleResponse?.link.orEmpty()).into(binding.memeImg)
        }
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        holder.bind(memes.items?.get(position))
    }

    override fun getItemCount(): Int = memes.items?.size?:0


}