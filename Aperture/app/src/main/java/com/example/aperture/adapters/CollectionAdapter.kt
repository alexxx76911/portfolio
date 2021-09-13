package com.example.aperture.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.aperture.R
import com.example.aperture.data.jsonclasses.PhotoCollection
import com.example.aperture.data.bdentities.PhotoCollectionEntity
import com.example.aperture.databinding.CollectionItemBinding
import com.example.aperture.databinding.MoreButtonLayoutBinding
import com.example.aperture.util.loadImage
import java.io.File

class CollectionAdapter(private val context: Context, private val onClick: (id: String, collectionPosition: Int, view: View) -> Unit, private val onMoreClick: () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var collectionList = listOf<PhotoCollection?>()
    private var bdCollectionList = listOf<PhotoCollectionEntity>()

    class CollectionViewHolder(private val context: Context, view: View, onClick: (position: Int, view: View) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: CollectionItemBinding by viewBinding()

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition, binding.root)
            }

        }

        fun bind(collection: PhotoCollection?) {
            collection ?: return
            binding.root.transitionName = collection.id
            binding.progressBar.isVisible = true
            binding.progressBar.indeterminateDrawable?.setTint(Color.DKGRAY)
            binding.photoCountTextView.text = "${collection.total_photos} ${context.getString(R.string.photosText)}"
            binding.collectionNameTextView.text = collection.title

            loadImage(collection.cover_photo.urls.raw, binding.photoImageView, onFail = {
                binding.errorTextView.text = context.getString(R.string.loadPhotoFail)
                binding.progressBar.isVisible = false
                true
            }, onSuccess = { resource ->
                binding.progressBar.isVisible = false
                binding.photoImageView.setImageDrawable(resource)
                true
            })

            collection.user.profile_image?.let {
                loadImage(it.small, binding.avatarImageView)
            }

            binding.authorNameTextView.text = collection.user.name
            binding.authorMailTextView.text = "@${collection.user.username}"
        }

        fun bind(collection: PhotoCollectionEntity) {
            binding.root.transitionName = collection.id
            binding.photoCountTextView.text = "${collection.photoCount} ${context.getString(R.string.photosText)}"
            binding.collectionNameTextView.text = collection.name
            loadImage(File(collection.coverPhotoPath), binding.photoImageView, onFail = {
                binding.errorTextView.text = context.getString(R.string.loadPhotoFail)
                binding.progressBar.isVisible = false
                true
            }, onSuccess = { resource ->
                binding.progressBar.isVisible = false
                binding.photoImageView.setImageDrawable(resource)
                true
            })
            collection.authorAvatar?.let {
                loadImage(it, binding.avatarImageView)
            }

            binding.authorNameTextView.text = collection.authorName
            binding.authorMailTextView.text = "@${collection.authorNickname}"

        }


    }

    class ButtonViewHolder(view: View, onMoreClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: MoreButtonLayoutBinding by viewBinding()

        init {
            binding.moreBtn.setOnClickListener {
                onMoreClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == COLLECTION_TYPE) {
            CollectionViewHolder(
                    context,
                    LayoutInflater.from(parent.context).inflate(R.layout.collection_item, parent, false)
            ) { position, view ->
                onClick(
                        if (collectionList.isNotEmpty()) {
                            collectionList[position]?.id.orEmpty()
                        } else {
                            bdCollectionList[position].id
                        },
                        position,
                        view
                )


            }
        } else {
            ButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.more_button_layout, parent, false), onMoreClick = { position ->
                onMoreClick()
            })
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is CollectionViewHolder) {
            return
        }
        if (collectionList.isNotEmpty()) {
            holder.bind(collectionList[position])
        } else {
            holder.bind(bdCollectionList[position])
        }
    }

    override fun getItemCount(): Int {
        return if (collectionList.isNotEmpty()) {
            collectionList.size
        } else {
            bdCollectionList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (collectionList.isNotEmpty() && collectionList[position] == null) {
            BUTTON_TYPE
        } else {
            COLLECTION_TYPE
        }
    }

    fun updateList(newList: List<PhotoCollection>) {
        if (newList.isEmpty()) {
            collectionList = emptyList()
            notifyDataSetChanged()
            return
        }
        collectionList = newList + listOf(null)
        notifyDataSetChanged()
    }

    fun updateListFromBd(newList: List<PhotoCollectionEntity>) {
        bdCollectionList = newList
        notifyDataSetChanged()
    }

    companion object {
        const val COLLECTION_TYPE = 1
        const val BUTTON_TYPE = 2

    }
}