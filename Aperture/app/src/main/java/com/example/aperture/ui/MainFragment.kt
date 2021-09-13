package com.example.aperture.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.MainActivity
import com.example.aperture.adapters.PhotoAdapter
import com.example.aperture.R
import com.example.aperture.databinding.FragmentMainBinding
import com.example.aperture.util.toPx
import com.example.aperture.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainFragmentViewModel by viewModels()
    private val binding: FragmentMainBinding by viewBinding()
    private var photoAdapter: PhotoAdapter by autoCleared()


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

        viewModel.isLogged()


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (requireActivity() as MainActivity).showNavigation()
        initList()
        initToolBar()
        initSwipeRefresh()
        bindViewModel()

        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

    }

    private fun initList() {
        val layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 8.toPx()
            marginStart = 4.toPx()
            marginEnd = 4.toPx()
        }

        val staggeredParams = StaggeredGridLayoutManager.LayoutParams(layoutParams)

        photoAdapter = PhotoAdapter(
                requireContext(),
                staggeredParams,
                onClick = { id, view ->
                    val photoDetailsTransitionName = "photoDetails"
                    val extras = FragmentNavigatorExtras(view to photoDetailsTransitionName)
                    val directions = MainFragmentDirections.actionMainFragmentToPhotoDetailsFragment(id)
                    findNavController().navigate(directions, extras)
                },
                onLike =
                { id, liked ->
                    viewModel.onLikeClick(id, liked)
                },
                onMoreClick = {
                    viewModel.onMorePhotos()
                })

        with(binding.tape) {
            adapter = photoAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)

        }
    }

    private fun bindViewModel() {
        viewModel.photos.observe(viewLifecycleOwner, Observer { newPhotos ->
            photoAdapter.updateList(newPhotos)

        })

        viewModel.photosBd.observe(viewLifecycleOwner, Observer { newPhotos ->
            photoAdapter.updateListFromBd(newPhotos)
        })



        viewModel.isLogged.observe(viewLifecycleOwner, Observer { isLogged ->
            if (isLogged) {
                viewModel.getFirstPage()
            } else {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToStartFragment())
            }

        })

        viewModel.snackBarLiveData.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
                view.background.alpha = 70
                setTextColor(Color.RED)
                show()
            }
        })

        viewModel.photoChanged.observe(viewLifecycleOwner, Observer { photo ->
            photo ?: return@Observer
            photoAdapter.updatePhoto(photo.photo)

        })


    }

    private fun initToolBar() {

        val searchView = binding.toolBar.menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnSearchClickListener {
            val searchFlow = callbackFlow<String> {
                val searchListener = object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        sendBlocking(newText.orEmpty())
                        return true
                    }

                }
                searchView.setOnQueryTextListener(searchListener)
                awaitClose { searchView.setOnQueryTextListener(null) }
            }

            viewModel.collectSearchFlow(searchFlow)
        }

    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFirstPage()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        viewModel.cancelSearchJob()
        super.onDestroyView()
    }


}