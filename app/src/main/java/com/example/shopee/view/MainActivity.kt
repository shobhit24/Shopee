package com.example.shopee.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.shopee.R
import com.example.shopee.databinding.ActivityMainBinding
import com.example.shopee.util.enums.SwipeDirection
import com.example.shopee.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point for the Shopee App.
 * This activity contains the setup for the following features:
 * - Setting up Navigation component
 * - Handling Swipe Gestures
 * - Handling Search Events
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private var menuItems: MutableList<MenuItem> = mutableListOf()
    private val productViewModel: ProductViewModel by viewModels()

    private val navController by lazy { navHostFragment.navController }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation Component
        setNavigationComponent()

        // Observing the Swipe Gestures
        handleSwipeGestures()

        // Search Products
        handelSearchEvents()
    }

    /**
     *  Sets the navigation component.
     *  - Sets the navigation graph
     *  - Attach the navController to the bottomNavigation in order to navigate to specific destination when clicked on bottom navigation items
     */

    private fun setNavigationComponent() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController.setGraph(R.navigation.dashboard_navigation)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.menu.forEach {
            menuItems.add(it)
        }
    }

    /**
     *  Handles the [swipe gestures] [SwipeDirection]. And calculates the destination menuItem using the [SwipeDirection]
     *  and pass the menuItem to [navigateTo].
     */
    fun handleSwipeGestures() {
        productViewModel.swipeObserver.observe(this) {
            val currentMenuItem =
                binding.bottomNavigation.menu.findItem(binding.bottomNavigation.selectedItemId)
            val index = menuItems.indexOf(currentMenuItem)
            when (it!!) {
                SwipeDirection.LEFT -> {
                    // SwipeLeft -> Swiping from Right to Left  -> Navigate back to previous bottom nav item
                    if (index < menuItems.size - 1) {
                        navigateTo(menuItems[index + 1])
                    }
                }

                SwipeDirection.RIGHT -> {
                    // SwipeRight -> Swiping from left to Right -> Navigate back to previous bottom nav item
                    if (index > 0) {
                        navigateTo(menuItems[index - 1])
                    }
                }
            }
        }
    }

    /**
     * Observes the textInput in the searchBar and passes the textInput to the viewModel
     */

    private fun handelSearchEvents() {
        binding.header.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productViewModel.getSearchResults(newText?.trim())
                return true
            }
        })

        // when swipeToRefresh -> isRefreshing is true, clear the searchText and clear the focus
        productViewModel.isRefreshing.observe(this) {
            it.let {
                binding.header.searchBar.isIconified = true
                binding.header.searchBar.clearFocus()
            }
        }
    }

    /**
     * Navigates to the desired menu item in the navigation graph
     * @param destination menuItem to which we have to navigate
     */

    private fun navigateTo(destination: MenuItem) {
        NavigationUI.onNavDestinationSelected(destination, navController)
    }
}