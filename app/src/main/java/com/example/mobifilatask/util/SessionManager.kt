package com.example.mobifilatask.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context:Context)
{
    private val pref:SharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    private val editor:SharedPreferences.Editor=pref.edit()
    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }
    fun createLoginSession(username:String,password:String)
    {
        editor.putBoolean(KEY_IS_LOGGED_IN,true)
        editor.putString(KEY_USERNAME,username)
        editor.putString(KEY_PASSWORD,password)
        editor.apply()
    }
    fun isLoggedIn():Boolean
    {
        return pref.getBoolean(KEY_IS_LOGGED_IN,false)
    }
    fun getUserName():String?{
        return pref.getString(KEY_USERNAME,null)
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }
}