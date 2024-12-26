package com.hms.cookingreceipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.databinding.ListItemBlogBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class BlogspotAdapter : RecyclerView.Adapter<BlogspotAdapter.BlogspotViewHolder>() {
    var entryList: List<Entry> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogspotViewHolder {
        val binding =
            ListItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogspotViewHolder(binding)
    }

    override fun getItemCount(): Int = entryList.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: BlogspotViewHolder, position: Int) {
        holder.bind(entryList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(entry: Entry)
    }

    private var listener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class BlogspotViewHolder(private val binding: ListItemBlogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: Entry) {
            binding.tvBlogTitle.text = entry.title.value
            binding.tvBlogDate.text =
                SimpleDateFormat("dd MMM, yyyy HH:MM:SS").format(entry.published.value)
            entry.media.url?.let {
                Picasso.get().load(it)
                    .into(binding.ivBlogImage)
            }
            binding.cdMain.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(entryList[position])
                }
            }
        }
    }
}