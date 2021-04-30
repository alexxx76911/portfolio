package com.example.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.homework.databinding.MovieItemBinding

class MovieAdapter(private val onDelete: ((movie: Movie) -> Unit)?) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movies = listOf<Movie>()


    class MovieViewHolder(view: View, onDelete: ((position: Int) -> Unit)?) :
        RecyclerView.ViewHolder(view) {
        private val viewBinding: MovieItemBinding by viewBinding()

        init {
            if (onDelete != null) {
                viewBinding.deleteBtn.setOnClickListener { onDelete(adapterPosition) }
            } else {
                viewBinding.deleteBtn.isVisible = false
            }
        }

        fun bind(movie: Movie) {
            viewBinding.titleTextView.text = movie.title
            viewBinding.yearTextView.text = movie.year
            Glide.with(itemView)
                .load(movie.poster)
                .into(viewBinding.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val deleteLmb = if (onDelete != null) {
            { position: Int ->
                onDelete?.invoke(movies[position])
            }
        } else {
            null
        }

        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false),
            deleteLmb
        )


    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])

    }

    override fun getItemCount(): Int {
        return movies.size

    }

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}