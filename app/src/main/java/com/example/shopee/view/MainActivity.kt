package com.example.shopee.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.shopee.util.OnSwipeTouchListener
import com.example.shopee.R
import com.example.shopee.databinding.ActivityMainBinding
import com.example.shopee.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private val navController by lazy { navHostFragment.navController }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController.setGraph(R.navigation.dashboard_navigation)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                navController.navigate(R.id.navigation_dashboard_profile)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                navController.navigate(R.id.navigation_dashboard_home)
            }

        })

    }
}