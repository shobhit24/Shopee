package com.example.shopee.view

import android.annotation.SuppressLint
import android.os.Bundle

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
        "ListFragment",
        "GridFragment"
    )

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private val productViewModel: ProductViewModel by viewModels()

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

        productViewModel.swipeLeftObserver.observe(this) {
            val index = itemsArray.indexOf(it)
            if (index < itemsArray.size - 1) {
                val finalString: String = itemsArray[index + 1]
                navigateTo(finalString)
            }
        }
        productViewModel.swipeRightObserver.observe(this) {
            val index = itemsArray.indexOf(it)
            if (index > 0) {
                val destination: String = itemsArray[index - 1]
                navigateTo(destination)
            }
        }
    }

    private fun navigateTo(destination: String) {
        when (destination) {
            "ListFragment" -> navController.navigate(R.id.navigation_dashboard_list)
            "GridFragment" -> navController.navigate(R.id.navigation_dashboard_grid)
        }
    }
}