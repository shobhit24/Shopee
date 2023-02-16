package com.example.shopee.features.productList.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopee.api.APiInterface
import com.example.shopee.api.ApiUtilities
import com.example.shopee.databinding.FragmentHomeBinding
import com.example.shopee.data.remote.model.Item
import com.example.shopee.view.ItemAdapter
import com.example.shopee.view.ViewType
import com.example.shopee.data.remote.repository.ItemRepository
import com.example.shopee.data.remote.viewModel.ItemViewModel
import com.example.shopee.data.remote.viewModel.ItemViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiInterface = ApiUtilities.getInstace().create(APiInterface::class.java)
        val itemRepository = ItemRepository(apiInterface)
        binding.linearRecyclerView.layoutManager = LinearLayoutManager(activity)
        itemViewModel = ViewModelProvider(
            this,
            ItemViewModelFactory(itemRepository)
        ).get(ItemViewModel::class.java)
        itemViewModel.items.observe(requireActivity()) {
            itemViewModel.items.value?.data?.items?.let {
                adapter = ItemAdapter(it, ViewType.List)
                binding.linearRecyclerView.adapter = adapter

            }
        }
    }
}