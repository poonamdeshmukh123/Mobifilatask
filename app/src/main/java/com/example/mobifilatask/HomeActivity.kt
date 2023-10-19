package com.example.mobifilatask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.mobifilatask.databinding.ActivityHomeBinding
import com.example.mobifilatask.fragment.DashBoardFragment
import com.example.mobifilatask.fragment.NameFragment
import com.example.mobifilatask.roomdb.*
import com.example.mobifilatask.util.SessionManager

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var sessionManager: SessionManager
    lateinit var db: MyEmpDatabase
    private lateinit var myViewModel: EmpViewModel
    lateinit var userDao: EmployeeDao
    lateinit var userdata: Employee
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(applicationContext)
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerlayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
        roomdbinit()
        loadFragment(DashBoardFragment())
        binding.navempinfo.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menufname -> loadFragment(DashBoardFragment())
                R.id.menulastname -> loadFragment(NameFragment())
                R.id.menuemail -> loadFragment(DashBoardFragment())
                R.id.menulogout ->

                    moveNextActivity()

                // Add more cases for other menu items
            }
            binding.drawerlayout.closeDrawer(GravityCompat.START)
            true
        }

        Toast.makeText(this, "wecome in homepage", Toast.LENGTH_LONG).show()
    }

    fun moveNextActivity() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("Are you sure you want to logout")


        // Set positive button and its click listener
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            // Do something when the positive button is clicked
            sessionManager.logoutUser()
            startActivity(Intent(this@HomeActivity, MainActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
            dialog.dismiss()
        }

        // Set negative button and its click listener (optional)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Do something when the negative button is clicked
            dialog.dismiss()
        }

        // Create and show the AlertDialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun loadFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame, fragment)
                .commit()
        }
    }


    private fun roomdbinit() {
        db = Room.databaseBuilder(
            applicationContext,
            MyEmpDatabase::class.java, "employee"
        )
            .allowMainThreadQueries().fallbackToDestructiveMigration()
            .build()
        userDao = db.employeeDao()
        val userRepository = EmployeeRepository(userDao)
        myViewModel = ViewModelProvider(
            this,
            EmployeeViewmodelFactory(userRepository)
        ).get(EmpViewModel::class.java)
        if (sessionManager.getUserName() != null) {
            userdata = userDao.getUserData(sessionManager.getUserName()!!)
        } else {
            Toast.makeText(this, "username is null", Toast.LENGTH_LONG).show()
        }

        val navigationmenu = binding.navempinfo.menu
        val newfname = navigationmenu.findItem(R.id.menufname)
        newfname.title = userdata.firstname
        val newlname = navigationmenu.findItem(R.id.menulastname)
        newlname.title = userdata.lastname
        val newemail = navigationmenu.findItem(R.id.menuemail)
        newemail.title = userdata.email
    }

}