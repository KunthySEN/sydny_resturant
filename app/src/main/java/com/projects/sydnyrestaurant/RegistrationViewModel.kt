package com.projects.sydnyrestaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel : ViewModel() {
    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    fun registerUser(email: String, password: String, confirmPassword: String) {
        if (password == confirmPassword) {
            // Logic to handle user registration (e.g., API call)
            // Simulating registration success for demonstration
            _registrationSuccess.value = true
        } else {
            _registrationSuccess.value = false
        }
    }
}