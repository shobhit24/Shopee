package com.example.shopee.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.shopee.R
import com.example.shopee.databinding.ActivityMainBinding
import com.example.shopee.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val itemsArray = arrayOf(
        "navigation_dashboard_list",
        "navigation_dashboard_grid"
    )

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var currentDestination: String

    val navController by lazy { navHostFragment.navController }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController.setGraph(R.navigation.dashboard_navigation)
        binding.bottomNavigation.setupWithNavController(navController)

        // Search products
//        binding.header.searchBar.clearFocus()
        binding.header.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productViewModel.searchProducts(newText)
                return true
            }
        })

        // SwipeLeft -> Swiping from Right to Left  -> Navigate back to previous bottom nav item
        // Observing the SwipeLeft LiveData
        productViewModel.swipeLeftObserver.observe(this) {
            currentDestination =
                navController.currentDestination?.displayName?.substringAfterLast(":id/").toString()
            val index = itemsArray.indexOf(currentDestination)

            // Checking current selected bottom nav item is not at the last item
            if (index < itemsArray.size - 1) {
                val finalDestination: String = itemsArray[index + 1]
                navigateTo(finalDestination)
            }
        }

        // SwipeRight -> Swiping from left to Right -> Navigate back to previous bottom nav item
        // Observing the SwipeRight LiveData
        productViewModel.swipeRightObserver.observe(this) {
            currentDestination =
                navController.currentDestination?.displayName?.substringAfterLast(":id/").toString()
            val index = itemsArray.indexOf(currentDestination)

            // Checking current selected bottom nav item is not at the first item
            if (index > 0) {
                val finalDestination: String = itemsArray[index - 1]
                navigateTo(finalDestination)
            }
        }

    }

    private fun navigateTo(finalDestination: String) {
        // navigating to the final destination fragment
        when (finalDestination) {
            "navigation_dashboard_list" -> navController.navigate(R.id.navigation_dashboard_list)
            "navigation_dashboard_grid" -> navController.navigate(R.id.navigation_dashboard_grid)
        }

    }
}