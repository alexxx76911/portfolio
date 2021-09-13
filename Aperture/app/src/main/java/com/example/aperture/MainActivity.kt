package com.example.aperture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.data.AppStatus
import com.example.aperture.databinding.ActivityMainBinding
import com.example.aperture.ui.MainFragmentDirections


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleIntent()
    }

    override fun onStart() {
        super.onStart()
        initBottomNavigation()
    }


    private fun initBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(findNavController(R.id.fragmentContainerView))
    }

    private fun handleIntent() {
        intent.data?.let {
            findNavController(R.id.fragmentContainerView).navigate(
                    MainFragmentDirections.actionMainFragmentToPhotoDetailsFragment(
                            it.lastPathSegment.orEmpty()
                    )
            )
        }
    }


    override fun onResume() {

        AppStatus.isAppVisible = true
        super.onResume()
    }

    override fun onPause() {
        AppStatus.isAppVisible = false
        super.onPause()
    }


    fun hideNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }


}