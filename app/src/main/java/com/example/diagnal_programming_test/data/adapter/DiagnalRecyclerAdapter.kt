package com.example.diagnal_programming_test.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.diagnal_programming_test.R
import com.example.diagnal_programming_test.data.model.Content
import com.example.diagnal_programming_test.databinding.ComedyItemBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DiagnalRecyclerAdapter @Inject constructor(private val context: Context) :
    ListAdapter<Content, ComedyViewHolder>(ComedyDiffCallback()) {
    private val TAG: String = DiagnalRecyclerAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComedyViewHolder {
        Log.d(TAG, "ComedyViewHolder: ")
        return ComedyViewHolder(
            ComedyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ComedyViewHolder, position: Int) {
        holder.bind(getItem(position),context)
    }
}

class ComedyViewHolder(
    private val binding: ComedyItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val TAG: String = ComedyViewHolder::class.java.name

    @SuppressLint("DiscouragedApi", "UseCompatLoadingForDrawables")
    fun bind(item: Content, context: Context) {
        binding.title.text = item.name
        val resId: Int = context.resources.getIdentifier(
            item.posterImage.dropLast(4),
            "drawable",
            context.packageName
        )

        if (resId != 0) {
            val drawable: Drawable = context.resources.getDrawable(resId, context.theme)
            Glide.with(binding.root.context).load(drawable).into(binding.comedyImg)
        } else {
            Glide.with(binding.root.context).load(R.drawable.poster1).into(binding.comedyImg)
            Log.d(TAG, "ComedyViewHolder: Drawable Resource not found")
        }

        Log.d(TAG, "ComedyViewHolder: $resId")
    }

}

class ComedyDiffCallback : DiffUtil.ItemCallback<Content>() {
    override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }
}

