package com.hms.cookingreceipes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.data.model.Entry
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_blog.view.*
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

    override fun onBindViewHolder(holder: BlogspotViewHolder, position: Int) {
        /*val split = entryList[position].content.value.trim().split("<br /><br />")
        var description = ""
        if (split.size > 3) {
            if (split[1].equals("")) {
                description = split[2]
            } else if (split[1].length < 100) {
                description = split[1] + " " + split[2]
            } else {
                description = split[1]
            }
        }*/

        val url = entryList[position].media.url
        holder.itemView.tvBlogTitle.text = entryList[position].title.value
        holder.itemView.tvBlogDate.text =
            SimpleDateFormat("dd MMM, yyyy HH:MM:SS").format(entryList[position].published.value)
        holder.itemView.tvBlogDescription.text = ""
        Picasso.get().load(entryList[position].media.url).into(holder.itemView.ivBlogImage)
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
            itemView.findViewById<MaterialCardView>(R.id.cdMain).setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(entryList.get(position))
                }
            }
        }
    }
}