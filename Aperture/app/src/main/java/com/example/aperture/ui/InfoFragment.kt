package com.example.aperture.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.R
import com.example.aperture.databinding.FragmentInfoBinding
import com.example.aperture.util.withArguments

class InfoFragment : Fragment(R.layout.fragment_info) {
    private val binding: FragmentInfoBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.infoTextView.setText(requireArguments().getInt(TEXT))
        if (requireArguments().getInt(POSITION) == 0) {
            binding.leftArrowImage.isVisible = false
        }


    }


    companion object {
        fun newInstance(
                @StringRes text: Int,
                position: Int
        ): InfoFragment {
            return InfoFragment().withArguments {
                putInt(TEXT, text)
                putInt(POSITION, position)
            }

        }

        const val TEXT = "text"
        const val POSITION = "position"
    }
}