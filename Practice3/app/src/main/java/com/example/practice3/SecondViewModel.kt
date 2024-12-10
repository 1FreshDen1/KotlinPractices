package com.example.pr3


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondViewModel : ViewModel() {
    private val _navigateToThirdFragment = MutableLiveData<Boolean>()
    val navigateToThirdFragment: LiveData<Boolean>
        get() = _navigateToThirdFragment

    fun onNavigateToThirdFragment() {
        _navigateToThirdFragment.value = true
    }

    fun onNavigateToThirdFragmentComplete() {
        _navigateToThirdFragment.value = false
    }
}