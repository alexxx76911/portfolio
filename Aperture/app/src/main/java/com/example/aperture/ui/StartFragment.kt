package com.example.aperture.ui

import android.os.Bundle

import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.MainActivity
import com.example.aperture.R
import com.example.aperture.adapters.ViewPagerAdapter
import com.example.aperture.databinding.FragmentStartBinding


class StartFragment : Fragment(R.layout.fragment_start) {
    private val binding: FragmentStartBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideNavigation()
        binding.viewPager.adapter = ViewPagerAdapter(
                listOf(
                        R.string.start_info1,
                        R.string.start_info2,
                        R.string.start_info3
                ),
                this
        )
    }
}