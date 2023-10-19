package com.example.mobifilatask.roomdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.mobifilatask.EmployeeRepository
import kotlinx.coroutines.launch


class EmpViewModel(val employeeRepository: EmployeeRepository) : ViewModel() {
    val allUsers: LiveData<List<Employee>> = liveData {
        emit(employeeRepository.getAllUsers())
    }

    fun insertUser(emp: Employee) {
        viewModelScope.launch {
            val user = Employee(
                firstname = emp.firstname,
                lastname = emp.lastname,
                email = emp.email,
                password = emp.password
            )
            employeeRepository.insertUser(user)
        }
    }

}

