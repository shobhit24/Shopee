package com.example.shopee.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopee.viewModel.ProductViewModel
import com.example.shopee.databinding.FragmentHomeBinding
import com.example.shopee.view.ProductAdapter
import com.example.shopee.view.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ProductAdapter
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linearRecyclerView.layoutManager = LinearLayoutManager(activity)

        productViewModel.products.observe(requireActivity()) {
            productViewModel.products.value?.data?.items?.let {
                adapter = ProductAdapter(it, ViewType.List)
                binding.linearRecyclerView.adapter = adapter

            }
        }
    }
}