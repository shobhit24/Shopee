package com.example.shopee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopee.R
import com.example.shopee.databinding.ProductGridTileBinding
import com.example.shopee.databinding.ProductListTileBinding
import com.example.shopee.model.Product
import com.example.shopee.util.enums.ViewType

/**
 *  ProductAdapter is responsible for creating ViewHolder for recyclerView list items and setting the data for those list items.
 *
 *  @param itemList List of products
 *  @param viewType type of layout manager [LinearLayoutManager] or [GridLayoutManager]
 */
class ProductAdapter(private val itemList: List<Product>, private val viewType: ViewType) :
    RecyclerView.Adapter<ProductAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, _viewType: Int
    ): ItemViewHolder {

        // Check for the viewType (List / Grid)
        return when (viewType) {

            // Return ItemViewHolder with ListTileBinding
            ViewType.List -> ItemViewHolder(
                ProductListTileBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ).root, viewType
            )

            // Return ItemViewHolder with GridTileBinding
            ViewType.Grid -> ItemViewHolder(
                ProductGridTileBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ).root, viewType
            )
        }

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        when (holder.itemViewType) {
            ViewType.List -> {

                // Bind view with ListTileBinding and add the values to tile properties
                val binding = ProductListTileBinding.bind(holder.itemView)
                binding.productName.text = currentItem.name
                binding.productPrice.text = currentItem.price
                if (currentItem.extra != null) {
                    binding.productShipping.text = currentItem.extra
                }

                // To load product Image
                loadImage(binding.productImage.context, currentItem.image, binding.productImage)
            }
            ViewType.Grid -> {

                // Bind view with GridTileBinding and add the values to tile properties
                val binding = ProductGridTileBinding.bind(holder.itemView)
                binding.productName.text = currentItem.name
                binding.productPrice.text = currentItem.price

                // To load product Image
                loadImage(binding.productImage.context, currentItem.image, binding.productImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    /**
     * Used to load the image in the ImageView with [Glide]
     *
     * @param context is the context of the view in which image has to be loaded
     * @param imageUrl is the url of the image which is is going to be loaded
     * @param imageView is the view in which image has to be loaded
     */
    private fun loadImage(context: Context, imageUrl: String?, imageView: ImageView) {
        Glide.with(context).load(if (imageUrl.isNullOrEmpty()) null else imageUrl)
            .placeholder(R.drawable.placeholder_image).error(R.drawable.placeholder_image)
            .into(imageView)
    }

    class ItemViewHolder(view: View, val itemViewType: ViewType) : RecyclerView.ViewHolder(view)
}