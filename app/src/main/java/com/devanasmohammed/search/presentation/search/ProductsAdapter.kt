package com.devanasmohammed.search.presentation.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.devanasmohammed.search.R
import com.devanasmohammed.search.data.model.Product

class ProductsAdapter(
    private val context: Context,
) : ListAdapter<Product, ProductsAdapter.ProductsViewHolder>(DiffCallback()) {

    inner class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resultsTv: TextView
        val content: ConstraintLayout
        val priceTv: TextView
        val rateTv: TextView
        val rateCounterTv: TextView
        val productTitle: TextView
        val productDescription: TextView
        val productImage: ImageView

        init {
            resultsTv = view.findViewById(R.id.results_tv)
            content = view.findViewById(R.id.content)
            priceTv = view.findViewById(R.id.price_tv)
            rateTv = view.findViewById(R.id.rate_tv)
            rateCounterTv = view.findViewById(R.id.rate_counter_tv)
            productTitle = view.findViewById(R.id.product_title)
            productDescription = view.findViewById(R.id.product_description)
            productImage = view.findViewById(R.id.product_image)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = getItem(position)

        //first item for results
        if (product.title.isEmpty() && product.brand.isNotEmpty()) {
            holder.resultsTv.visibility = View.VISIBLE
            holder.content.visibility = View.GONE

            holder.resultsTv.text = "Found\n${product.brand} results"
        } else {
            holder.resultsTv.visibility = View.GONE
            holder.content.visibility = View.VISIBLE

            holder.apply {
                productTitle.text = product.title
                productDescription.text = product.description
                rateTv.text = product.rating.toString()
                rateCounterTv.text = context.getString(R.string.fake_rating_counter)
                priceTv.text = product.price.toString()
            }
            setupGlide(product.thumbnail, holder.productImage)
        }
    }

    private fun setupGlide(url: String, thumbnail: ImageView) {
        Glide.with(context)
            .load(url)
//            .placeholder(getShimmerDrawable())
//            .error(R.drawable.ic_no_photo)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
//                    holder.shimmer.hideShimmer()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    holder.shimmer.hideShimmer()
                    return false
                }

            })
            .into(thumbnail)
    }

}