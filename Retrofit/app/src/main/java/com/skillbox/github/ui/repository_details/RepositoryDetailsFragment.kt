package com.skillbox.github.ui.repository_details

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.skillbox.github.R
import kotlinx.android.synthetic.main.fragment_repo_details.*

class RepositoryDetailsFragment : Fragment(R.layout.fragment_repo_details) {

    private val viewModel: RepoDetailsViewModel by viewModels()

    private val args: RepositoryDetailsFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        repoNameTextView.text = "Название: ${args.name}"
        idTextView.text = "id: ${args.id}"
        ownerTextView.text = "Владелец: ${args.owner}"
        isStarred()

        viewModel.starInfo.observe(viewLifecycleOwner) { isStarred ->
            starCheckBox.isEnabled = true
            starCheckBox.isChecked = isStarred

        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        }


        starCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (starCheckBox.isPressed) {
                viewModel.putOrRemoveStar(args.owner, args.name, isChecked)
            }

        }


    }

    private fun isStarred() {
        viewModel.getStarInfo(args.owner, args.name)
    }


}