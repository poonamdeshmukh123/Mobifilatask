package com.example.mobifilatask

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.mobifilatask.api.model.LoginRequest
import com.example.mobifilatask.api.myinterface.GetRequestResponse
import com.example.mobifilatask.api.viewmodel.LoginApiViewmodel
import com.example.mobifilatask.databinding.ActivityMainBinding
import com.example.mobifilatask.roomdb.*
import com.example.mobifilatask.util.SessionManager

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: LoginApiViewmodel
    lateinit var db: MyEmpDatabase
    private lateinit var myViewModel: EmpViewModel
    lateinit var userDao: EmployeeDao
    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(LoginApiViewmodel::class.java)

        session = SessionManager(applicationContext)
        //room db instance initialization
        roomdbInit()
        binding.loginbtn.setOnClickListener {
            //check validation
            val check = validlogin(
                binding.useredit.text.toString().trim(),
                binding.passedit.text.toString().trim()
            )
            if (check) {
                //check user is registered or not
                if (userDao.login(
                        binding.useredit.text.toString().trim(),
                        binding.passedit.text.toString().trim()
                    )
                ) {
                    //call api
                    getLoginApicall()

                } else {
                    Toast.makeText(this, "Invalid Username or password", Toast.LENGTH_LONG).show()
                }

            }
        }
        binding.signupaccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun getLoginApicall() {
        val reqobj = LoginRequest(
            binding.useredit.text.toString().trim(),
            binding.passedit.text.toString().trim()
        )
        viewModel.getLoginResponse(reqobj, object : GetRequestResponse {
            override fun onResponse(json: String) {
                session.createLoginSession(
                    binding.useredit.text.toString().trim(),
                    binding.passedit.text.toString().trim()
                )
                binding.useredit.text = null
                binding.passedit.text = null
                Toast.makeText(this@MainActivity, "Login Successfully", Toast.LENGTH_LONG).show()
                // Redirect to the main activity
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }

            override fun onError(json: String) {
                Toast.makeText(this@MainActivity, "error: " + json, Toast.LENGTH_LONG).show()
            }

        })
    }


    fun validlogin(usernm1: String, password1: String): Boolean {

        return if (usernm1.length == 0) {
            binding.useredit.requestFocus()
            binding.useredit.setError("must be enter the email")
            false
        } else if (!usernm1.matches(Regex("[a-zA-Z0-9]+@gmail+\\.+com+"))) {
            //Patterns.EMAIL_ADDRESS.matcher(usernm1).matches() inbuild way to email
            binding.useredit.requestFocus()
            binding.useredit.setError("enter the correct mail")
            false
        } else if (password1.length == 0) {
            binding.passedit.requestFocus()
            binding.passedit.setError("must be enter the password:")
            false
        } else {
            true
        }
    }

    fun roomdbInit() {
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
}