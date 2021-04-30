package com.example.homework


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*


class MainFragmentViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val moviesLiveData = MutableLiveData<List<Movie>>()
    private val searchingLiveData = MutableLiveData<Boolean>()
    private lateinit var job: Job

    val movies: LiveData<List<Movie>>
        get() = moviesLiveData

    val searching: LiveData<Boolean>
        get() = searchingLiveData


    fun bind(
        queryFlow: Flow<String>,
        movieTypeFlow: Flow<MovieType>
    ): Flow<Pair<String, MovieType>> {
        return combine(
            queryFlow,
            movieTypeFlow.onStart { emit(MovieType.Movie) }) { query, movieType ->
            query to movieType
        }

    }

    fun collectSearchFlow(flow: Flow<Pair<String, MovieType>>) {
        job = flow
            .debounce(1000)
            .mapLatest { pair ->
                searchingLiveData.postValue(true)
                repository.searchMovies(pair.first, pair.second.string)
            }
            .onEach {
                moviesLiveData.postValue(it)
                searchingLiveData.postValue(false)
            }
            .launchIn(viewModelScope)

    }

    fun cancelJob() {
        job.cancel()
    }
}