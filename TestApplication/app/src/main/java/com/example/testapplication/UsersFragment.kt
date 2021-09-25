package com.example.testapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.testapplication.databinding.FragmentUsersBinding
import com.example.testapplication.util.autoCleared

class UsersFragment : Fragment(R.layout.fragment_users) {
    private val binding: FragmentUsersBinding by viewBinding()
    private val viewModel: UsersFragmentViewModel by viewModels()
    private var usersAdapter: UsersAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
    }

    private fun initList() {
        usersAdapter = UsersAdapter(
            requireContext(),
            onClick = { person ->
                findNavController().navigate(
                    UsersFragmentDirections.actionUsersFragmentToUserDetailsFragment(
                        userFirstName = person.first_name,
                        userLastName = person.last_name,
                        userEmail = person.email,
                        userAvatar = person.avatar
                    )
                )

            })


        with(binding.usersList) {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.getUsers()
    }

    private fun bindViewModel() {
        viewModel.users.observe(viewLifecycleOwner, Observer { persons ->
            usersAdapter.updateList(persons)
        })

        viewModel.toast.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.isVisible = isLoading
        })

    }
}