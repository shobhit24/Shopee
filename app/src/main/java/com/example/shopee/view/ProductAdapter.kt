package com.example.shopee.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.shopee.remote.model.Product
import com.example.shopee.databinding.ProductGridTileBinding
import com.example.shopee.databinding.ProductListTileBinding

enum class ViewType() {
    List, Grid,
}

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
                Glide.with(binding.productImage.context).load(currentItem.image)
                    .into(binding.productImage)
            }
            ViewType.Grid -> {

                // Bind view with GridTileBinding and add the values to tile properties
                val binding = ProductGridTileBinding.bind(holder.itemView)
                binding.productName.text = currentItem.name
                binding.productPrice.text = currentItem.price

                // To load product Image
                Glide.with(binding.productImage.context).load(currentItem.image)
                    .into(binding.productImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(view: View, val itemViewType: ViewType) : RecyclerView.ViewHolder(view)
}