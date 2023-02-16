package com.example.shopee.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopee.viewModel.ProductViewModel
import com.example.shopee.view.util.ProductViewModelFactory
import com.example.shopee.databinding.FragmentHomeBinding
import com.example.shopee.view.ProductAdapter
import com.example.shopee.view.ProductApplication
import com.example.shopee.view.ViewType

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productRepository =
            (requireActivity().application as ProductApplication).productRepository

        binding.linearRecyclerView.layoutManager = LinearLayoutManager(activity)

        productViewModel = ViewModelProvider(
            this, ProductViewModelFactory(productRepository)
        ).get(ProductViewModel::class.java)
        productViewModel.items.observe(requireActivity()) {
            productViewModel.items.value?.data?.items?.let {
                adapter = ProductAdapter(it, ViewType.List)
                binding.linearRecyclerView.adapter = adapter

            }
        }
    }
}