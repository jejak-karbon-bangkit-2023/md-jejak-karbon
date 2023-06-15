package com.jejakkarbon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jejakkarbon.databinding.ItemGuideBinding
import com.jejakkarbon.model.Guide

class GuideAdapter(private val listGuide: List<Guide>) :
    RecyclerView.Adapter<GuideAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(var binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val binding =
            ItemGuideBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val guide = listGuide[position]
        Glide.with(holder.itemView.context)
            .load(guide.image_url).into(holder.binding.ivGuideImage)
        holder.binding.tvGuideTitle.text = guide.title
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(guide) }
    }

    override fun getItemCount(): Int = listGuide.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Guide)
    }
}