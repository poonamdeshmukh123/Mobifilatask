package com.example.mobifilatask.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmployeeDao {
    //interact with db
    @Insert
    suspend fun insert(emp: Employee)

    @Query("SELECT * FROM employee")
    suspend fun getAllEmployee(): List<Employee>

    //sign up unique username/email
    @Query("SELECT EXISTS (SELECT * FROM employee where email=:email)")
    fun is_Taken(email: String): Boolean

    //login
    @Query("SELECT EXISTS( SELECT * from employee where email=:email AND password=:password)")
    fun login(email: String, password: String): Boolean

    //fetch login user record
    @Query("SELECT * from employee where email=:email")
    fun getUserData(email: String): Employee


}