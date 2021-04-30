package com.example.homework

import android.text.Editable
import android.text.TextWatcher

import android.widget.EditText
import android.widget.RadioGroup
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                sendBlocking(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        this@textChangedFlow.addTextChangedListener(listener)

        awaitClose { this@textChangedFlow.removeTextChangedListener(listener) }
    }
}

fun RadioGroup.checkedChangeFlow(): Flow<MovieType> {
    return callbackFlow {
        val listener = RadioGroup.OnCheckedChangeListener { _, p1 ->
            val type = when (p1) {
                R.id.movieRadioBtn -> MovieType.Movie
                R.id.seriesRadioBtn -> MovieType.Series
                R.id.episodeRadioBtn -> MovieType.Episode
                else -> error("")
            }
            sendBlocking(type)
        }
        this@checkedChangeFlow.setOnCheckedChangeListener(listener)
        awaitClose { this@checkedChangeFlow.setOnCheckedChangeListener(null) }
    }
}