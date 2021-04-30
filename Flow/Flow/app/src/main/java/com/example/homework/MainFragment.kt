package com.example.homework

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.homework.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewBinding: FragmentMainBinding by viewBinding()
    private val viewModel: MainFragmentViewModel by viewModels()
    private var movieAdapter: MovieAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieAdapter = MovieAdapter(null)
        initList()
        bindViewModel()
        initSearch()


        viewBinding.showMoviesBdBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.container, MoviesDbFragment())
                .addToBackStack("fragment").commit()
        }
    }

    private fun initSearch() {
        val flow = viewModel.bind(
            viewBinding.searchEditText.textChangedFlow(),
            viewBinding.radioGroup.checkedChangeFlow()
        )
        viewModel.collectSearchFlow(flow)
    }

    private fun bindViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movieAdapter?.updateMovies(movies)
            if (movies.isEmpty()) {
                Toast.makeText(requireContext(), "фильмы не найдены", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.searching.observe(viewLifecycleOwner, Observer { isSearching ->
            viewBinding.progressBar.isVisible = isSearching
        })
    }

    private fun initList() {
        with(viewBinding.moviesList) {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        viewModel.cancelJob()
        movieAdapter = null
        super.onDestroyView()
    }


}