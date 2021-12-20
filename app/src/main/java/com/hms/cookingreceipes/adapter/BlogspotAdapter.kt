package com.hms.cookingreceipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.data.model.Entry
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class BlogspotAdapter : RecyclerView.Adapter<BlogspotAdapter.BlogspotViewHolder>() {
    var entryList: List<Entry> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogspotViewHolder {
        return BlogspotViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_blog,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = entryList.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: BlogspotViewHolder, position: Int) {

        holder.itemView.findViewById<TextView>(R.id.tvBlogTitle).text =
            entryList[position].title.value
        holder.itemView.findViewById<TextView>(R.id.tvBlogDate).text =
            SimpleDateFormat("dd MMM, yyyy HH:MM:SS").format(entryList[position].published.value)
        val thumbUrl = entryList[position].media!!.url
        if (!thumbUrl.isNullOrBlank())
            Picasso.get().load(thumbUrl)
                .into(holder.itemView.findViewById<ImageView>(R.id.ivBlogImage))
    }

    interface OnItemClickListener {
        fun onItemClick(entry: Entry)
    }

    private var listener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class BlogspotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<CardView>(R.id.cdMain).setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(entryList[position])
                }
            }
        }
    }
}