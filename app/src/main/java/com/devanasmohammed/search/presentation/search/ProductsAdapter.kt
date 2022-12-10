package com.devanasmohammed.search.presentation.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.devanasmohammed.search.data.model.Product
import com.devanasmohammed.search.databinding.ItemProductBinding

class ProductsAdapter(
    private val context: Context,
) : ListAdapter<Product, ProductsAdapter.ProductsViewHolder>(DiffCallback()) {

    private lateinit var binding : ItemProductBinding

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    inner class ProductsViewHolder(val view: ItemProductBinding)
        : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
         binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = getItem(position)
        holder.view.product = product

        //first item for results
        if (product.title.isEmpty() && product.brand.isNotEmpty()) {
            binding.resultsTv.visibility = View.VISIBLE
            binding.content.visibility = View.GONE
        } else {
            binding.resultsTv.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
            setupGlide(product.thumbnail, binding.productImage)
        }
    }

    private fun setupGlide(url: String, thumbnail: ImageView) {
        Glide.with(context)
            .load(url)
            .into(thumbnail)
    }

}