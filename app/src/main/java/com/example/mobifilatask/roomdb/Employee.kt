package com.example.mobifilatask.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "employee")
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstname: String,
    val lastname: String,
    val email:String,
    val password:String

)