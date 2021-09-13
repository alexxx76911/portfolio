package com.example.aperture.ui

import android.graphics.Color
import android.os.Bundle

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.adapters.CollectionAdapter
import com.example.aperture.R
import com.example.aperture.databinding.FragmentCollectionsBinding
import com.example.aperture.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis

class CollectionsFragment : Fragment(R.layout.fragment_collections) {
    private val binding: FragmentCollectionsBinding by viewBinding()
    private val viewModel: CollectionsFragmentViewModel by viewModels()
    private var collectionAdapter: CollectionAdapter by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300
        }

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 300
        }

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 300
        }

        viewModel.getFirstPage()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        bindViewModel()
        initList()
        initSwipeRefresh()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

    }


    private fun initList() {
        collectionAdapter = CollectionAdapter(
                requireContext(),
                onClick = { id, position, view ->
                    val collectionPhotosTransitionName = "collectionPhotos"
                    val extras = FragmentNavigatorExtras(view to collectionPhotosTransitionName)
                    val directions = CollectionsFragmentDirections.actionCollectionsFragmentToCollectionPhotosFragment(id, position)
                    findNavController().navigate(directions, extras)
                }, onMoreClick = {
            viewModel.onMoreCollections()
        })
        with(binding.collectionsList) {
            adapter = collectionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }


    }


    private fun bindViewModel() {
        viewModel.collections.observe(viewLifecycleOwner, Observer { collections ->
            collectionAdapter.updateList(collections)


        })
        viewModel.collectionsBd.observe(viewLifecycleOwner, Observer { collections ->
            collectionAdapter.updateListFromBd(collections)

        })

        viewModel.snackBarLiveData.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
                view.background.alpha = 70
                setTextColor(Color.RED)
                show()
            }
        })


    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFirstPage()
            binding.swipeRefresh.isRefreshing = false
        }

    }


}