package com.example.shopee.view

import com.example.shopee.databinding.ItemTileGridBinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopee.databinding.ItemTileListBinding
import com.example.shopee.data.remote.model.Item

enum class ViewType(val value: Int) {
    List(0),
    Grid(1),
}

class ItemAdapter(private val itemList: List<Item>, private val viewType: ViewType) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        _viewType: Int
    ): ItemViewHolder {

        return when (viewType) {
            ViewType.List ->
                ItemViewHolder(
                    ItemTileListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ).root,
                    viewType
                )
            ViewType.Grid ->
                ItemViewHolder(
                    ItemTileGridBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ).root,
                    viewType
                )
        }

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        when (holder.itemViewType) {
            ViewType.List -> {
                val binding = ItemTileListBinding.bind(holder.itemView)
                binding.itemName.text = currentItem.name
                binding.itemPrice.text = currentItem.price
                if (currentItem.extra != null) {
                    binding.itemShipping.text = currentItem.extra
                }
                Glide.with(binding.itemImage.context).load(currentItem.image).into(binding.itemImage);
            }
            ViewType.Grid -> {
                val binding = ItemTileGridBinding.bind(holder.itemView)
                binding.itemName.text = currentItem.name
                binding.itemPrice.text = currentItem.price
                Glide.with(binding.itemImage.context).load(currentItem.image).into(binding.itemImage);
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ItemViewHolder(binding: View, val itemViewType: ViewType) :
        RecyclerView.ViewHolder(binding)
}