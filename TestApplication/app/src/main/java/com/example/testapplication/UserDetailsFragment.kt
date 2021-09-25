package com.example.testapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.testapplication.databinding.FragmentUserDetailsBinding

class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {
    private val binding: FragmentUserDetailsBinding by viewBinding()
    private val args: UserDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInfo()
    }


    private fun bindInfo() {
        Glide.with(requireView())
            .load(args.userAvatar)
            .into(binding.avatar)

        binding.userName.text =
            requireContext().getString(R.string.user_name, args.userLastName, args.userFirstName)
        binding.userEmail.text = args.userEmail
    }
}