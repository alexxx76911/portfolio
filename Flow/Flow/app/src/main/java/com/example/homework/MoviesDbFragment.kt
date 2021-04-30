package com.example.homework

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.homework.databinding.FragmentMoviesDbBinding

class MoviesDbFragment : Fragment(R.layout.fragment_movies_db) {
    private val viewBinding: FragmentMoviesDbBinding by viewBinding()
    private val viewModel: MoviesDbFragmentViewModel by viewModels()
    private var movieAdapter: MovieAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieAdapter = MovieAdapter { movie ->
            viewModel.deleteMovieFromDb(movie)
        }
        bindViewModel()
        initList()
        viewModel.observeMoviesDb()
    }

    private fun initList() {
        with(viewBinding.moviesDbList) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

    }

    private fun bindViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movieAdapter?.updateMovies(movies)
            if (movies.isEmpty()) {
                Toast.makeText(requireContext(), "бд пуста", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onDestroyView() {
        movieAdapter = null
        super.onDestroyView()
    }
}