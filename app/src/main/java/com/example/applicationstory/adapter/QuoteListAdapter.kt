package com.example.applicationstory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.applicationstory.data.response.QuoteResponseItem
import com.example.applicationstory.databinding.ItemListStoryBinding
import com.example.applicationstory.formatter.DateFormatter
import java.util.TimeZone

class QuoteListAdapter :
    PagingDataAdapter<QuoteResponseItem, QuoteListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickcallBack: OnItemClickcallBack? = null

    fun setOnItemClickCallback (onItemClickcallBack: OnItemClickcallBack) {
        this.onItemClickcallBack = onItemClickcallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(private val binding: ItemListStoryBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: QuoteResponseItem) {

            binding.root.setOnClickListener {
                onItemClickcallBack?.onItemClicked(data)
            }

            binding.tvItemUsername.text = data.name
            val maxLines = 1
            val maxCharsPerLine = 35

            val originalDescription = data.description
            val lines = originalDescription!!.split("\n")

            var truncatedDescription = ""

            for (i in 0 until maxLines) {
                if (i < lines.size) {
                    val line = lines[i]
                    if (line.length <= maxCharsPerLine) {
                        truncatedDescription += line
                    } else {
                        truncatedDescription += line.substring(0, maxCharsPerLine) + "..."
                    }
                    if (i != lines.size - 1) {
                        truncatedDescription += "...\n"
                    }
                }
            }

            binding.tvItemDescription.text = truncatedDescription.trim()
            val dateFormatter = DateFormatter
            val formattedDate = data.createdAt?.let { dateFormatter.formatDate(it, TimeZone.getDefault().id) }
            binding.tvItemDate.text = formattedDate
            Glide.with(itemView)
                .load(data.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.tvItemPhoto)

        }
    }

    interface OnItemClickcallBack{
        fun onItemClicked(data: QuoteResponseItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<QuoteResponseItem>() {
            override fun areItemsTheSame(oldItem: QuoteResponseItem, newItem: QuoteResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: QuoteResponseItem, newItem: QuoteResponseItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}