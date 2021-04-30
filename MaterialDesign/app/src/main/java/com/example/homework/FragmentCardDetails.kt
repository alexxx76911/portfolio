package com.example.homework

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.homework.databinding.FragmentCardDetailsBinding
import com.google.android.material.transition.MaterialContainerTransform

class FragmentCardDetails : Fragment(R.layout.fragment_card_details) {
    private val args: FragmentCardDetailsArgs by navArgs()
    private val viewBinding: FragmentCardDetailsBinding by viewBinding()
    private val viewModel: FragmentCardDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val card = viewModel.getCardById(args.id)

        viewBinding.titleTextView.text = card.title
        viewBinding.secondaryTextView.text = card.price
        viewBinding.descriptionTextView.text = card.description
        Glide.with(this)
                .load(card.image)
                .into(viewBinding.image)
    }

}