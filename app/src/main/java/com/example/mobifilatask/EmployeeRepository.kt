package com.example.mobifilatask

import com.example.mobifilatask.roomdb.Employee
import com.example.mobifilatask.roomdb.EmployeeDao

class EmployeeRepository(val userDao: EmployeeDao)
{
    suspend fun getAllUsers(): List<Employee> {

      return  userDao.getAllEmployee()
    }

    suspend fun insertUser(user: Employee) {
userDao.insert(user)
    }

}
