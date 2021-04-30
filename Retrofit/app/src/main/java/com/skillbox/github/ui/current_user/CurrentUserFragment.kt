package com.skillbox.github.ui.current_user

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.skillbox.github.R
import com.skillbox.github.User
import kotlinx.android.synthetic.main.fragment_current_user.*

class CurrentUserFragment : Fragment(R.layout.fragment_current_user) {

    private val viewModel: CurrentUserViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressBar.animate()

        viewModel.getUserInfo()

        viewModel.userData.observe(viewLifecycleOwner) { user ->
            progressBar.isVisible = false

            user ?: Toast.makeText(
                requireContext(),
                "не удалось загрузить пользователя",
                Toast.LENGTH_LONG
            ).show()
            if (user != null) {
                bind(user)
            }

        }

        viewModel.errorData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
        }


    }


    private fun bind(user: User) {
        Log.d("user", "bind")
        loginTextView.text = "логин: ${user.login}"
        reposTextView.text = "количество репозиториев: ${user.reposNumber}"
        Glide.with(this)
            .load(user.avatar)
            .into(avatarImageView)


    }

}