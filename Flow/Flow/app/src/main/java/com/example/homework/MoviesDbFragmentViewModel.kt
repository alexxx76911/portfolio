package com.example.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MoviesDbFragmentViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val moviesLiveData = MutableLiveData<List<Movie>>()

    val movies: LiveData<List<Movie>>
        get() = moviesLiveData


    fun observeMoviesDb() {
        repository.observeMoviesDb()
            .onEach { moviesLiveData.postValue(it) }
            .launchIn(viewModelScope)
    }


    fun deleteMovieFromDb(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovieFromDb(movie)
        }
    }


}