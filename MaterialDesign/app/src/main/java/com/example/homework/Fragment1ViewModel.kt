package com.example.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Fragment1ViewModel : ViewModel() {
    private val repository = CardRepository()
    private val cardLiveData = MutableLiveData<List<CardObject>>()

    val cards: LiveData<List<CardObject>>
        get() = cardLiveData

    fun getCards() {
        cardLiveData.postValue(repository.getCards())
    }

}