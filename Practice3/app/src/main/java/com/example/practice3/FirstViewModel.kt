package com.example.practice3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstViewModel : ViewModel() {
    private val _navigateToSecondFragment = MutableLiveData<Boolean>()
    val navigateToSecondFragment: LiveData<Boolean>
        get() = _navigateToSecondFragment

    fun onNavigateToSecondFragment() {
        _navigateToSecondFragment.value = true
    }

    fun onNavigateToSecondFragmentComplete() {
        _navigateToSecondFragment.value = false
    }
}