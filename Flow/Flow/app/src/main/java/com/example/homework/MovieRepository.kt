package com.example.homework


import com.example.homework.network.Network
import java.lang.Exception

class MovieRepository {
    private val moviesDao = DataBase.instance.moviesDao()

    suspend fun searchMovies(query: String, type: String): List<Movie> {
        return try {
            val movies =
                Network.moviesApi.getMovies(title = query, type = type).Search ?: emptyList()
            if (movies.isNotEmpty()) {
                saveMovies(movies)
            }
            movies
        } catch (e: Exception) {
            searchMoviesDb(query, type).orEmpty()
        }

    }

    private suspend fun saveMovies(movies: List<Movie>) {
        val moviesDb = moviesDao.getAllMovies()
        val moviesToSave = if (moviesDb == null) {
            movies
        } else {
            movies - moviesDb
        }

        moviesDao.insertMovies(moviesToSave.toSet().toList())

    }

    private suspend fun searchMoviesDb(query: String, type: String): List<Movie>? {
        return moviesDao.getAllMovies()
            ?.filter { it.title.contains(query, ignoreCase = true) && it.type == type }
    }

    fun observeMoviesDb() = moviesDao.observeMoviesDb()

    suspend fun deleteMovieFromDb(movie: Movie) = moviesDao.deleteMovie(movie)


}