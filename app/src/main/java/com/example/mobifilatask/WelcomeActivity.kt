package com.example.mobifilatask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mobifilatask.databinding.ActivityWelcomeBinding
import com.example.mobifilatask.util.SessionManager

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityWelcomeBinding
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(applicationContext)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                sessionmanagment()

            }

        }, 3000)
    }

    private fun sessionmanagment() {

        if (session.isLoggedIn()) {
            startActivity(Intent(this@WelcomeActivity, HomeActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        } else {
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }
    }

}