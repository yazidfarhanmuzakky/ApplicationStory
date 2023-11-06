//package com.example.applicationstory.view.main
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.example.applicationstory.data.response.StoryItem
//import com.example.applicationstory.databinding.ItemListStoryBinding
//import com.example.applicationstory.formatter.DateFormatter
//import java.util.TimeZone
//
//class MainDataAdapter : RecyclerView.Adapter<MainDataAdapter.DataViewHolder>() {
//
//    private val list = ArrayList<StoryItem>()
//    private var onItemClickcallBack: OnItemClickcallBack? = null
//    private val dateFormatter = DateFormatter
//
//    fun setOnItemClickCallback (onItemClickcallBack: OnItemClickcallBack) {
//        this.onItemClickcallBack = onItemClickcallBack
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setList(getAllStoriesResponse: ArrayList<StoryItem>) {
//        list.clear()
//        list.addAll(getAllStoriesResponse)
//        notifyDataSetChanged()
//    }
//
//    inner class DataViewHolder(val binding: ItemListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(listStories: StoryItem) {
//            binding.root.setOnClickListener {
//                onItemClickcallBack?.onItemClicked(listStories)
//            }
//            binding.apply {
//                tvItemUsername.text = listStories.name
//                val maxLines = 1
//                val maxCharsPerLine = 35
//
//                val originalDescription = listStories.description
//                val lines = originalDescription.split("\n")
//
//                var truncatedDescription = ""
//
//                for (i in 0 until maxLines) {
//                    if (i < lines.size) {
//                        val line = lines[i]
//                        if (line.length <= maxCharsPerLine) {
//                            truncatedDescription += line
//                        } else {
//                            truncatedDescription += line.substring(0, maxCharsPerLine) + "..."
//                        }
//                        if (i != lines.size - 1) {
//                            truncatedDescription += "...\n"
//                        }
//                    }
//                }
//
//                tvItemDescription.text = truncatedDescription.trim()
//                val formattedDate = dateFormatter.formatDate(listStories.createdAt, TimeZone.getDefault().id)
//                tvItemDate.text = formattedDate
//                Glide.with(itemView)
//                    .load(listStories.photoUrl)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(tvItemPhoto)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
//        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return DataViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
//        holder.bind(list[position])
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    interface OnItemClickcallBack{
//        fun onItemClicked(data: StoryItem)
//    }
//}