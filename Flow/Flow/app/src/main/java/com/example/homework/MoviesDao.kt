package com.example.homework

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM ${MovieContract.TABLE_NAME}")
    suspend fun getAllMovies(): List<Movie>?

    @Query("SELECT * FROM ${MovieContract.TABLE_NAME}")
    fun observeMoviesDb(): Flow<List<Movie>>

    @Delete
    suspend fun deleteMovie(movie: Movie)

}