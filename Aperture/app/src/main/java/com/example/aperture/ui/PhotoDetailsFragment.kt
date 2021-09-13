package com.example.aperture.ui


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.aperture.R
import com.example.aperture.databinding.FragmentPhotoDetailsBinding
import com.example.aperture.util.DownloadWorker
import com.example.aperture.util.haveQ
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform


class PhotoDetailsFragment : Fragment(R.layout.fragment_photo_details) {
    private val binding: FragmentPhotoDetailsBinding by viewBinding()
    private val args: PhotoDetailsFragmentArgs by navArgs()
    private val viewModel: PhotoDetailsFragmentViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 200
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolBar.apply {
            setOnMenuItemClickListener {
                viewModel.sharePhoto(args.photoId)
                true
            }
        }
        binding.progressBar.indeterminateDrawable?.setTint(Color.DKGRAY)
        binding.downloadTextView.setOnClickListener {
            if (haveQ().not()) {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                viewModel.savePhoto(args.photoId)
                observeDownloading()
            }
        }
        binding.userLikeImageView.setOnClickListener {
            viewModel.onLikeClick(args.photoId)
        }

        bindViewModel()
        viewModel.getPhotoInfo(args.photoId)
        initSwipeRefresh()
        initWritePermissionLauncher()
    }


    private fun bindViewModel() {
        viewModel.photoInfo.observe(viewLifecycleOwner, Observer { photo ->
            showErrorLayout(false)
            photo.location?.country?.let {
                val city = if (photo.location.city != null) {
                    "${photo.location.city},"
                } else {
                    ""
                }
                binding.locationTextView.text = "$city ${photo.location.country}"
                binding.locationTextView.setOnClickListener {
                    if (photo.location.position?.latitude != null) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data =
                                Uri.parse("geo:${photo.location.position.latitude},${photo.location.position.longitude}")

                        }
                        if (intent.resolveActivity(requireContext().packageManager) != null) {
                            startActivity(intent)
                        }
                    }


                }
            }

            if (binding.photoImageView.drawable == null) {
                binding.progressBar.isVisible = true
                Glide.with(requireView())
                    .load(photo.urls.raw)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.isVisible = false
                            binding.errorTextView.isVisible = true
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.isVisible = false
                            binding.photoImageView.setImageDrawable(resource)
                            return true
                        }

                    })
                    .into(binding.photoImageView)
            }


            Glide.with(requireView())
                .load(photo.user.profile_image?.small)
                .into(binding.avatarImageView)

            setLikeImage(photo.liked_by_user)


            binding.authorNameTextView.text = photo.user.name
            binding.authorMailTextView.text = "@${photo.user.username}"
            binding.likesNumberTextView.text = photo.likes.toString()


            photo.exif?.let {
                binding.exifTextView.text = """
                Made with: ${it?.make ?: "-"}
                Model: ${it?.model ?: "-"}
                Exposure: ${it?.exposure_time ?: "-"}
                Aperture: ${it?.aperture ?: "-"}
                Focal length: ${it?.focal_length ?: "-"}
                ISO: ${it?.iso ?: "-"}
            """.trimIndent()
            }

            photo.tags?.let {
                var tagsStr = ""
                it.forEach { tag ->
                    tagsStr += " #${tag.title}"
                }

                binding.tagsTextView.text = tagsStr
            }

            photo.user.bio?.let { bio ->
                binding.aboutAuthorTextView.text = "About @${photo.user.username}: ${bio}"
            }

            photo.downloads?.let {
                val str = "Download"
                binding.downloadTextView.text = "$str ($it)".toSpannable().apply {
                    setSpan(UnderlineSpan(), 0, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

        })


        viewModel.shareIntent.observe(viewLifecycleOwner, Observer { intent ->
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        })

        viewModel.snackBar.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
                view.background.alpha = 70
                setTextColor(Color.RED)
                show()
            }

        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            showErrorLayout(true)
        })


    }

    private fun setLikeImage(liked: Boolean) {
        val likeImageRes = if (liked) {
            R.drawable.ic_baseline_favorite_24
        } else {
            R.drawable.ic_baseline_favorite_border_24
        }
        binding.userLikeImageView.setImageResource(likeImageRes)
    }


    private fun observeDownloading() {
        viewModel.workInfoLiveData?.observe(viewLifecycleOwner, Observer { workInfo ->

            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                Snackbar.make(
                    requireView(),
                    requireContext().getString(R.string.snackBarDownloadSuccess),
                    Snackbar.LENGTH_LONG
                ).apply {
                    view.background.alpha = 70
                }
                    .setAction(requireContext().getString(R.string.snackBarOpenAction)) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            data =
                                Uri.parse(workInfo.outputData.getString(DownloadWorker.FILE_CONTENT_URI))
                        }
                        if (intent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(intent)
                        }

                    }
                    .show()
            }



            if (workInfo.state == WorkInfo.State.FAILED)
                Snackbar.make(
                    requireView(),
                    requireContext().getString(R.string.downloadFail),
                    Snackbar.LENGTH_SHORT
                ).apply {
                    view.background.alpha = 70
                    setTextColor(Color.RED)
                    show()
                }
        })

    }

    private fun showErrorLayout(show: Boolean) {
        binding.errorTextView.isVisible = show
        binding.layout.isVisible = !show
        binding.downloadIcon.isVisible = !show
        binding.downloadTextView.isVisible = !show
    }


    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getPhotoInfo(args.photoId)
            binding.swipeRefresh.isRefreshing = false
        }

    }

    private fun initWritePermissionLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    viewModel.savePhoto(args.photoId)
                    observeDownloading()
                }
            }
    }
}