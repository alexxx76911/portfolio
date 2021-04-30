package com.example.homework

import androidx.lifecycle.ViewModel

class FragmentCardDetailsViewModel : ViewModel() {
    private val repository = CardRepository()

    fun getCardById(id: Long): CardObject {
        return repository.getCardById(id.toInt())
    }
}