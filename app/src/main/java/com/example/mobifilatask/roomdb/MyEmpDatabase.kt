package com.example.mobifilatask.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Employee::class], version = 1)
abstract class MyEmpDatabase:RoomDatabase()
{
    abstract fun employeeDao():EmployeeDao

}