package com.example.aperture.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.R
import com.example.aperture.data.jsonclasses.Photo
import com.example.aperture.data.bdentities.PhotoEntity
import com.example.aperture.databinding.MoreButtonLayoutBinding
import com.example.aperture.databinding.PhotoItemBinding
import com.example.aperture.util.loadImage
import com.example.aperture.util.toPx
import java.io.File

class PhotoAdapter(
        private val context: Context,
        private val layoutParams: ViewGroup.MarginLayoutParams,
        private val onClick: (id: String, view: View) -> Unit,
        private val onLike: (id: String, liked: Boolean) -> Unit,
        private val onMoreClick: () -> Unit
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var photos = mutableListOf<Photo?>()
    private var photosBd = listOf<PhotoEntity>()


    class PhotoViewHolder(
            private val context: Context,
            view: View,
            layoutParams: ViewGroup.LayoutParams,
            onClick: (position: Int, view: View) -> Unit,
            private val onLike: (position: Int) -> Unit
    ) :
            RecyclerView.ViewHolder(view) {
        private val binding: PhotoItemBinding by viewBinding()

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition, binding.root)
            }
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                itemView.layoutParams = StaggeredGridLayoutManager.LayoutParams(layoutParams as ViewGroup.MarginLayoutParams)
            } else {
                itemView.layoutParams = layoutParams
            }

        }

        fun bind(photo: Photo?) {
            photo ?: return
            binding.root.transitionName = photo.id

            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                        adapterPosition == 0
                binding.photoImageView.layoutParams.height =
                        200.toPx()
            } else {
                binding.photoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            binding.progressBar.isVisible = true
            binding.progressBar.indeterminateDrawable?.setTint(Color.DKGRAY)
            val likeImageRes = if (photo.liked_by_user) {
                R.drawable.ic_baseline_favorite_24
            } else {
                R.drawable.ic_baseline_favorite_border_24
            }

            loadImage(photo.urls.raw, binding.photoImageView, onFail = {
                binding.errorTextView.text = context.getString(R.string.loadPhotoFail)
                binding.progressBar.isVisible = false
                true
            }, onSuccess = { resource ->
                binding.progressBar.isVisible = false
                binding.photoImageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.photoImageView.setImageDrawable(resource)
                true
            })
            binding.userLikeImageView.setImageResource(likeImageRes)
            binding.userLikeImageView.setOnClickListener {
                onLike(adapterPosition)
            }
            photo.user.profile_image?.let { loadImage(it.small, binding.avatarImageView) }
            binding.authorNameTextView.text = photo.user.name
            binding.authorMailTextView.text = "@${photo.user.username}"
            binding.likesNumberTextView.text = photo.likes.toString()
            photo.downloads?.let {
                binding.downloadTextView.text = "Download (${it})"
            }


        }

        fun bind(photo: PhotoEntity) {
            binding.root.transitionName = photo.id
            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                        adapterPosition == 0
                binding.photoImageView.layoutParams.height =
                        200.toPx()
            } else {
                binding.photoImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            }

            binding.progressBar.indeterminateDrawable?.setTint(Color.DKGRAY)
            val likeImageRes = if (photo.likedByUser == 1) {
                R.drawable.ic_baseline_favorite_24
            } else {
                R.drawable.ic_baseline_favorite_border_24
            }
            loadImage(File(photo.imagePath), binding.photoImageView, onFail = {
                binding.errorTextView.text = context.getString(R.string.loadPhotoFail)
                binding.progressBar.isVisible = false
                true
            }, onSuccess = { resource ->
                binding.progressBar.isVisible = false
                binding.photoImageView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.photoImageView.setImageDrawable(resource)
                true

            })
            binding.userLikeImageView.setImageResource(likeImageRes)
            photo.authorAvatar?.let { loadImage(it, binding.avatarImageView) }
            binding.authorNameTextView.text = photo.authorName
            binding.authorMailTextView.text = "@${photo.authorNickname}"
            binding.likesNumberTextView.text = photo.likes.toString()
        }


    }

    class ButtonViewHolder(view: View, onMoreClick: () -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: MoreButtonLayoutBinding by viewBinding()

        init {
            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            }
            binding.moreBtn.setOnClickListener {
                onMoreClick()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PHOTO_TYPE) {
            PhotoViewHolder(
                    context,
                    LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false),
                    layoutParams,
                    { position, view ->
                        onClick(
                                if (photos.isNotEmpty()) {
                                    photos[position]?.id.orEmpty()
                                } else {
                                    photosBd[position].id
                                },
                                view
                        )
                    }, { position ->
                val id: String
                val liked: Boolean
                if (photos.isNotEmpty()) {
                    id = photos[position]?.id.orEmpty()
                    liked = photos[position]?.liked_by_user ?: false
                } else {
                    id = photosBd[position].id
                    liked = photosBd[position].likedByUser == 1
                }
                onLike(id, liked)
            })
        } else {
            ButtonViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.more_button_layout, parent, false)
            ) {
                onMoreClick()
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is PhotoViewHolder) {
            return
        }
        if (photos.isNotEmpty()) {
            holder.bind(photos[position])
        } else {
            holder.bind(photosBd[position])
        }

    }

    override fun getItemCount(): Int {
        return if (photos.isNotEmpty()) {
            photos.size
        } else {
            photosBd.size
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (photos.isNotEmpty() && photos[position] == null) {
            BUTTON_TYPE
        } else {
            PHOTO_TYPE
        }
    }


    fun updateList(list: List<Photo>) {
        if (list.isEmpty()) {
            photos = emptyList<Photo>().toMutableList()
            notifyDataSetChanged()
            return
        }
        photos.remove(null)
        photos = (list + listOf(null)).toMutableList()
        notifyDataSetChanged()

    }


    fun updatePhoto(updatedPhoto: Photo) {
        val position = photos.indexOfFirst { photo ->
            photo?.id == updatedPhoto.id
        }
        if (photos.isNotEmpty()) {
            photos[position] = updatedPhoto
            notifyItemChanged(position)
        }
    }


    fun updateListFromBd(list: List<PhotoEntity>) {
        photosBd = list
        notifyDataSetChanged()
    }

    companion object {
        const val PHOTO_TYPE = 1
        const val BUTTON_TYPE = 2
    }
}