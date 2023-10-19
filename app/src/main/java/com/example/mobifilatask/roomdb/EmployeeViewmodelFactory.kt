package com.example.mobifilatask.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobifilatask.EmployeeRepository


// UserViewModelFactory.kt
class EmployeeViewmodelFactory(private val empRepository: EmployeeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmpViewModel(empRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

