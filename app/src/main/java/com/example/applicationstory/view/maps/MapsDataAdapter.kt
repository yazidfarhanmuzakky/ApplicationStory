//package com.example.applicationstory.maps
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.example.applicationstory.R
//import com.example.applicationstory.data.response.StoryItem
//import com.example.applicationstory.data.response.maps.MapsStoryItem
//import com.example.applicationstory.databinding.ItemListStoryBinding
//import java.util.TimeZone
//
//class MapsDataAdapter(private val storyItems: List<MapsStoryItem>) :
//    RecyclerView.Adapter<MapsDataAdapter.ViewHolder>() {
//
//    // Inner ViewHolder class
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.activity_maps, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val storyItem = storyItems[position]
//        holder.bind(storyItem)
//    }
//
//    override fun getItemCount(): Int {
//        return storyItems.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(storyItem: MapsStoryItem) {
//            // Bind data to UI elements here
//        }
//    }
//}