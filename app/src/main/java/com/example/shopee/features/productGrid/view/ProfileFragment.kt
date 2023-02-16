package com.example.shopee.features.productGrid.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopee.api.APiInterface
import com.example.shopee.api.ApiUtilities
import com.example.shopee.databinding.FragmentProfileBinding
import com.example.shopee.data.remote.model.Item
import com.example.shopee.data.remote.repository.ItemRepository
import com.example.shopee.data.remote.viewModel.ItemViewModel
import com.example.shopee.data.remote.viewModel.ItemViewModelFactory
import com.example.shopee.databinding.FragmentHomeBinding
import com.example.shopee.view.ItemAdapter
import com.example.shopee.view.ViewType

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var itemViewModel: ItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiInterface = ApiUtilities.getInstace().create(APiInterface::class.java)
        val itemRepository = ItemRepository(apiInterface)
        binding.gridRecyclerView.layoutManager = GridLayoutManager(activity, 3)
        itemViewModel = ViewModelProvider(
            this,
            ItemViewModelFactory(itemRepository)
        ).get(ItemViewModel::class.java)
        itemViewModel.items.observe(requireActivity()) {
            itemViewModel.items.value?.data?.items?.let {
                adapter = ItemAdapter(it, ViewType.Grid)
                binding.gridRecyclerView.adapter = adapter

            }
        }
    }
}