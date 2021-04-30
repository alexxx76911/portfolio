package com.skillbox.github.ui.repository_list

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.skillbox.github.R
import com.skillbox.github.ReposListAdapter
import kotlinx.android.synthetic.main.fragment_repository_list.*

class RepositoryListFragment : Fragment(R.layout.fragment_repository_list) {
    private var reposAdapter: ReposListAdapter? = null
    private val viewModel: RepositoryListViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar2.animate()

        reposAdapter = ReposListAdapter() { id, name, owner ->
            val action =
                RepositoryListFragmentDirections.actionRepositoryListFragmentToRepositoryDetailsFragment(
                    name,
                    id,
                    owner
                )
            findNavController().navigate(action)

        }


        with(reposList) {
            adapter = reposAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.repos.observe(viewLifecycleOwner) { foundRepos ->
            progressBar2.isVisible = false
            if (foundRepos != null) {
                reposAdapter?.updateList(foundRepos)
            } else {
                Toast.makeText(
                    requireContext(),
                    "не удалось загрузить репозитории",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        }

        viewModel.getRepos()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        reposAdapter = null
    }

}