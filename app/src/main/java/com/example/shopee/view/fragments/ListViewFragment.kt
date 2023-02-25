package com.example.shopee.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopee.R
import com.example.shopee.viewModel.ProductViewModel
import com.example.shopee.adapter.ProductAdapter
import com.example.shopee.databinding.FragmentListViewBinding
import com.example.shopee.util.OnSwipeTouchListener
import com.example.shopee.util.ProductListState
import com.example.shopee.util.enums.ErrorType
import com.example.shopee.util.enums.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListViewFragment : Fragment() {

    private lateinit var binding: FragmentListViewBinding
    private lateinit var adapter: ProductAdapter
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentListViewBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linearRecyclerView.layoutManager = LinearLayoutManager(activity)

        productViewModel.products.observe(viewLifecycleOwner) {
            binding.loaderView.progressBar.visibility = View.GONE
            binding.errorView.root.visibility = View.GONE
            binding.linearRecyclerView.visibility = View.GONE
            when (it) {
                is ProductListState.Loading -> {
                    binding.loaderView.progressBar.visibility = View.VISIBLE
                }
                is ProductListState.Success -> {
                    adapter = ProductAdapter(it.responseDTO.data.items, ViewType.List)
                    binding.linearRecyclerView.adapter = adapter
                    binding.linearRecyclerView.visibility = View.VISIBLE
                }
                is ProductListState.Error -> {
                    if (it.error.name == ErrorType.NETWORK_ERROR.name) {
                        binding.errorView.animationView.setAnimation(R.raw.no_internet)
                    } else {
                        binding.errorView.animationView.setAnimation(R.raw.item_not_found)
                    }
                    binding.errorView.root.visibility = View.VISIBLE
                    binding.errorView.errorText.text = it.error.errorMessage
                }
            }
        }

        binding.linearRecyclerView.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                // passing the current fragment to VM
                productViewModel.onSwipeLeft()
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            productViewModel.refreshProductList()
        }

        productViewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = it
        }
    }
}