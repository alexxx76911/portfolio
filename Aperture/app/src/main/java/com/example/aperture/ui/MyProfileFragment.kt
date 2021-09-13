package com.example.aperture.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.aperture.adapters.CollectionAdapter
import com.example.aperture.adapters.PhotoAdapter
import com.example.aperture.R
import com.example.aperture.databinding.FragmentProfileBinding
import com.example.aperture.util.toPx
import com.example.aperture.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis

class MyProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel: MyProfileFragmentViewModel by viewModels()
    private var photoAdapter: PhotoAdapter by autoCleared()
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

        viewModel.getProfileInfo()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initToolbar()
        initList()
        initSwipeRefresh()
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        binding.myPhotosTextView.setOnClickListener {
            viewModel.onFirstUserPhotos()
        }
        binding.likedPhotosTextView.setOnClickListener {
            viewModel.onFirstLikedPhotos()
        }
        binding.collectionsTextView.setOnClickListener {
            viewModel.onFirstUserCollections()
        }

    }


    private fun bindViewModel() {
        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            profile ?: return@Observer
            binding.swipeRefresh.isRefreshing = false
            showErrorLayout(false)
            binding.nameTextView.text = "${profile.first_name} ${profile.last_name}"
            binding.nickNameTextView.text = "@${profile.username}"
            binding.emailTextView.text = profile.email
            binding.downLoadsCountTextView.text = profile.downloads.toString()
            binding.bioTextView.text = profile.bio.orEmpty()
            binding.myPhotosTextView.text = """${profile.total_photos}
                |${requireContext().getString(R.string.myPhotosTextView)}
            """.trimMargin()

            binding.likedPhotosTextView.text = """${profile.total_likes}
                |${requireContext().getString(R.string.likedPhotosTextView)}
            """.trimMargin()

            binding.collectionsTextView.text = """${profile.total_collections}
                |${requireContext().getString(R.string.myCollectionsTextView)}
            """.trimMargin()

            profile.location?.let { location ->
                val city = if (location.city != null) {
                    "${location.city},"
                } else {
                    ""
                }
                binding.locationTextView.text =
                        "$city ${location.country ?: ""}"
                binding.locationTextView.setOnClickListener {
                    if (location.position?.latitude != null) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data =
                                    Uri.parse("geo:${location.position.latitude},${location.position.longitude}")

                        }
                        if (intent.resolveActivity(requireContext().packageManager) != null) {
                            startActivity(intent)
                        }
                    }


                }
            }


        })

        viewModel.profileImageUrl.observe(viewLifecycleOwner, Observer { url ->
            Glide.with(requireView())
                    .load(url)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(binding.avatarImageView)
        })

        viewModel.userPhotos.observe(viewLifecycleOwner, Observer { photos ->
            if (viewModel.getButtonEnabled() != 1) {
                return@Observer
            }
            if (binding.itemList.adapter !is PhotoAdapter) {
                binding.itemList.adapter = photoAdapter
            }
            photoAdapter.updateList(photos)
            binding.emptyListTextView.isVisible = photos.isEmpty()

        })

        viewModel.userLikedPhotos.observe(viewLifecycleOwner, Observer { photos ->
            if (viewModel.getButtonEnabled() != 2) {
                return@Observer
            }
            if (binding.itemList.adapter !is PhotoAdapter) {
                binding.itemList.adapter = photoAdapter
            }
            photoAdapter.updateList(photos)
            binding.emptyListTextView.isVisible = photos.isEmpty()
        })

        viewModel.userCollections.observe(viewLifecycleOwner, Observer { collections ->
            if (viewModel.getButtonEnabled() != 3) {
                return@Observer
            }
            if (binding.itemList.adapter !is CollectionAdapter) {
                binding.itemList.adapter = collectionAdapter
            }
            collectionAdapter.updateList(collections)
            binding.emptyListTextView.isVisible = collections.isEmpty()
        })


        viewModel.logOut.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(MyProfileFragmentDirections.actionMyProfileFragmentToStartFragment())
        })

        viewModel.snackbar.observe(viewLifecycleOwner, Observer { message ->
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

        viewModel.buttonEnabled.observe(viewLifecycleOwner, Observer { button ->
            binding.myPhotosTextView.alpha = 0.5f
            binding.likedPhotosTextView.alpha = 0.5f
            binding.collectionsTextView.alpha = 0.5f
            when (button) {
                1 -> binding.myPhotosTextView.alpha = 1f


                2 -> binding.likedPhotosTextView.alpha = 1f


                3 -> binding.collectionsTextView.alpha = 1f

            }
        })

        viewModel.onLoadinError.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = false
            showErrorLayout(true)
        })


    }

    private fun initList() {
        collectionAdapter = CollectionAdapter(
                requireContext(),
                onClick = { id, position, view ->
                    val collectionDetailsTransitionName = "collectionPhotos"
                    val extras = FragmentNavigatorExtras(view to collectionDetailsTransitionName)
                    val directions = MyProfileFragmentDirections.actionMyProfileFragmentToCollectionPhotosFragment(id, position)
                    findNavController().navigate(directions, extras)
                }, onMoreClick = {
            viewModel.onMore()

        })
        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400.toPx())
        photoAdapter = PhotoAdapter(
                requireContext(),
                layoutParams, onClick = { id, view ->
            val photoDetailsTransitionName = "photoDetails"
            val extras = FragmentNavigatorExtras(view to photoDetailsTransitionName)
            val directions = MyProfileFragmentDirections.actionMyProfileFragmentToPhotoDetailsFragment(id)
            findNavController().navigate(directions, extras)
        },
                onLike = { id, liked ->
                    viewModel.onLikeClick(id, liked)
                },
                onMoreClick = {
                    viewModel.onMore()
                }
        )

        with(binding.itemList) {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }


    }

    private fun initToolbar() {
        binding.toolBar.setOnMenuItemClickListener {
            LogoutDialog {
                viewModel.onLogout()
            }.show(childFragmentManager, "")
            true
        }
    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getProfileInfo()
        }
    }

    private fun showErrorLayout(show: Boolean) {
        binding.scrollView.isVisible = !show
        binding.errorTextView.isVisible = show
        binding.swipeRefresh.isVisible = show
    }

}