package com.example.mobifilatask

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import androidx.room.Room
import com.example.mobifilatask.databinding.ActivitySignUpBinding
import com.example.mobifilatask.roomdb.*
import com.example.mobifilatask.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var db: MyEmpDatabase
    private lateinit var myViewModel: EmpViewModel
    lateinit var userDao: EmployeeDao
    lateinit var session: SessionManager

    companion object {
        var isallowed = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = SessionManager(applicationContext)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //room db instance
        roomdbinit()
        binding.submitsignup.setOnClickListener {
            val check: Boolean = valid(
                binding.editfirstname.text.toString().trim(),
                binding.editlastname.text.toString().trim(),
                binding.editemail.text.toString().trim(),
                binding.editpassword.text.toString().trim()
            )
            //check username is allready exist or not
            if (!userDao.is_Taken(binding.editemail.text.toString().trim())) {
                isallowed = true
            } else {
                isallowed = false
                Toast.makeText(
                    this@SignUpActivity,
                    "username " + binding.editemail.text.toString().trim() + " is allready exist",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (check == true && isallowed == true) {

                openDialog()
            }
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

    }


    fun openDialog() {
        Toast.makeText(this, "signup sucessfully", Toast.LENGTH_LONG).show()


        val user = Employee(
            firstname = binding.editfirstname.text.toString().trim(),
            lastname = binding.editlastname.text.toString().trim(),
            email = binding.editemail.text.toString().trim(),
            password = binding.editpassword.text.toString().trim()
        )
        myViewModel.insertUser(user)


        session.createLoginSession(
            binding.editemail.text.toString().trim(),
            binding.editpassword.text.toString().trim()
        )
        binding.editfirstname.text = null
        binding.editlastname.text = null
        binding.editemail.text = null
        binding.editpassword.text = null
        // Redirect to the main activity
        startActivity(Intent(this@SignUpActivity, HomeActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
//        myViewModel.allUsers.observe(this, Observer { emp ->
//            Log.d("data", emp.get(0).firstname + " ," + emp.get(0).email)
//            binding.editfirstname.text = null
//            binding.editlastname.text = null
//            binding.editemail.text = null
//            binding.editpassword.text = null
//        })


    }

    fun valid(fname1: String, lname1: String, email1: String, password1: String): Boolean {
        return if (fname1.length == 0) {
            binding.editfirstname.requestFocus()
            binding.editfirstname.setError("must be enter the first name:")
            false
        } else if (!fname1.matches(Regex("[a-zA-Z]+"))) {
            binding.editfirstname.requestFocus()
            binding.editfirstname.setError("enter only character:")
            false
        } else if (lname1.length == 0) {
            binding.editlastname.requestFocus()
            binding.editlastname.setError("must be enter the last name:")
            false
        } else if (!lname1.matches(Regex("[a-zA-Z]+"))) {
            binding.editlastname.requestFocus()
            binding.editlastname.setError("enter only character:")
            false
        } else if (email1.length == 0) {

            binding.email.requestFocus()
            binding.email.setError("must be enter the email")

            false
        } else if (!email1.matches(Regex("[a-zA-Z0-9]+@gmail+\\.+com+"))) {
            binding.email.requestFocus()
            binding.email.setError("enter the correct mail")
            false
        } else if (password1.length == 0) {
            binding.password.requestFocus()
            binding.password.setError("must be enter the password:")
            false
        } else {
            true
        }
    }
}