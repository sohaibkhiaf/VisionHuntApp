package com.example.visionhunt

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.visionhunt.databinding.ActivityMainBinding
import com.example.visionhunt.ui.fragments.CameraFragment
import com.example.visionhunt.ui.fragments.TasksFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val tasksFragment by lazy {
        TasksFragment().apply {
            onStartCameraClick = {
                binding.bottomNavigation.selectedItemId = R.id.nav_camera
            }
        }
    }

    private val cameraFragment by lazy {
        CameraFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        @Suppress("DEPRECATION")
        window.statusBarColor = Color.parseColor("#F8FAFC")
        @Suppress("DEPRECATION")
        window.navigationBarColor = Color.parseColor("#FFFFFF")

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true
        insetsController.isAppearanceLightNavigationBars = true

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(tasksFragment)
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tasks -> {
                    loadFragment(tasksFragment)
                    true
                }
                R.id.nav_camera -> {
                    loadFragment(cameraFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
