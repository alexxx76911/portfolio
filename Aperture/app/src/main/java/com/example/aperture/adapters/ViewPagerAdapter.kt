package com.example.aperture.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aperture.ui.AuthFragment
import com.example.aperture.ui.InfoFragment

class ViewPagerAdapter(private val strings: List<Int>, fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return strings.size + 1

    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 3) {
            AuthFragment()
        } else {
            InfoFragment.newInstance(strings[position], position)
        }
    }

}