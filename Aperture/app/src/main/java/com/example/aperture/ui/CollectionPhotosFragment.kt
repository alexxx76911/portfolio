package com.example.aperture.ui


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.aperture.adapters.PhotoAdapter
import com.example.aperture.R
import com.example.aperture.databinding.FragmentCollectionPhotosBinding
import com.example.aperture.util.toPx
import com.example.aperture.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import java.io.File

class CollectionPhotosFragment : Fragment(R.layout.fragment_collection_photos) {
    private val binding: FragmentCollectionPhotosBinding by viewBinding()
    private val viewModel: CollectionPhotosFragmentViewModel by viewModels()
    private val args: CollectionPhotosFragmentArgs by navArgs()
    private var photoAdapter: PhotoAdapter by autoCleared()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300
        }

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 500
        }

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 500
        }


        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300
            scrimColor = Color.TRANSPARENT
        }

        getCollectionInfo()
        viewModel.getFirstCollectionPhotos()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initList()
        initSwipeRefresh()
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }


    }

    private fun bindViewModel() {
        viewModel.collection.observe(viewLifecycleOwner, Observer { collection ->
            binding.toolBar.title = collection.title
            Glide.with(requireView())
                    .load(collection.cover_photo.urls.raw)
                    .into(binding.coverImageView)



            binding.titleTextView.text = collection.title
            binding.descriptionTextView.text = collection.description
            binding.imagesCountTextView.text =
                    "${collection.total_photos} images by @${collection.user.username}"

        })

        viewModel.collectionBd.observe(viewLifecycleOwner, Observer { collection ->
            Glide.with(requireView())
                    .load(File(collection.coverPhotoPath))
                    .into(binding.coverImageView)



            binding.titleTextView.text = collection.name
            binding.descriptionTextView.text = collection.description.orEmpty()
            binding.imagesCountTextView.text =
                    "${collection.photoCount} images"


        })

        viewModel.collectionTags.observe(viewLifecycleOwner, Observer { tags ->
            var tagsStr = ""
            tags.forEach { tag ->
                tagsStr += " #${tag.title}"
            }

            binding.tagsTextView.text = tagsStr

        })

        viewModel.collectionPhotos.observe(viewLifecycleOwner, Observer { photos ->
            photoAdapter.updateList(photos)

        })

        viewModel.collectionPhotosBd.observe(viewLifecycleOwner, Observer { photos ->
            photoAdapter.updateListFromBd(photos)

        })

        viewModel.collectionPhotoChanged.observe(viewLifecycleOwner, Observer { photo ->
            photo ?: return@Observer
            photoAdapter.updatePhoto(photo.photo)

        })

        viewModel.snackbar.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
                view.background.alpha = 70
                setTextColor(Color.RED)
                show()
            }

        })
    }

    private fun getCollectionInfo() {
        viewModel.getCollectionInfo(args.collectionId, args.collectionPosition)
    }

    private fun initList() {
        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350.toPx())
        photoAdapter = PhotoAdapter(
                requireContext(),
                layoutParams,
                onClick = { id, view ->
                    val photoDetailsTransitionName = "photoDetails"
                    val extras = FragmentNavigatorExtras(view to photoDetailsTransitionName)
                    val directions = CollectionPhotosFragmentDirections.actionCollectionPhotosFragmentToPhotoDetailsFragment(id)
                    findNavController().navigate(directions, extras)

                },
                onLike = { id, liked ->
                    viewModel.onLikeClick(id, liked)
                },
                onMoreClick = {
                    viewModel.onMore()
                }
        )

        with(binding.collectionPhotoList) {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }


    }


    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFirstCollectionPhotos()
            binding.swipeRefresh.isRefreshing = false
        }
    }


}