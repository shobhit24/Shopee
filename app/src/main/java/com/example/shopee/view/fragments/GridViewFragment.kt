package com.example.shopee.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopee.viewModel.ProductViewModel
import com.example.shopee.adapter.ProductAdapter
import com.example.shopee.adapter.ViewType
import com.example.shopee.databinding.FragmentGridViewBinding
import com.example.shopee.util.OnSwipeTouchListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GridViewFragment : Fragment() {

    private lateinit var binding: FragmentGridViewBinding
    private lateinit var adapter: ProductAdapter
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentGridViewBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gridRecyclerView.layoutManager = GridLayoutManager(activity, 3)

        productViewModel.products.observe(requireActivity()) {
            productViewModel.products.value?.data?.items?.let {
                adapter = ProductAdapter(it, ViewType.Grid)
                binding.gridRecyclerView.adapter = adapter

            }
        }

        binding.gridRecyclerView.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                // passing the current fragment to VM
                productViewModel.onSwipeRight()
            }
        })
    }
}