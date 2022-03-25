package com.example.chitchat

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

//    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
val EMAIL_ADDRESS_PATTERN = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
    private lateinit var mauth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        mauth = Firebase.auth

        btn_signUp.setOnClickListener {
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }
        
        btn_signIN.setOnClickListener { 
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(this,"Enter Valid Data",Toast.LENGTH_SHORT).show()

            }
            else if(!isValidString(email)){

                 Toast.makeText(this,"Invalid email",Toast.LENGTH_SHORT).show()

            }
            else if(password.length<6){
                Toast.makeText(this,"Password length should be atleast 6 character",Toast.LENGTH_LONG).show()
            }
            else
            login(email,password)


        }

    }

    private fun isValidString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }


    private fun login(email: String, password: String) {

        mauth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val i = Intent(this,MainActivity::class.java)
                    startActivity(i);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"User does not exist", Toast.LENGTH_SHORT).show()

                }
            }


    }
}