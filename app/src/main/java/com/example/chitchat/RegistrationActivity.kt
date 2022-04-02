package com.example.chitchat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_signUp
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {
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
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()
        mauth = Firebase.auth


        btn_RsignIn.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)

        }

        btn_RsignUp.setOnClickListener {
            val email = edt_Remail.text.toString()
            val password = edt_Rpassword.text.toString()
            val compassword = edt_Rcompassword.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty((compassword))){
                Toast.makeText(this,"Enter Valid Data", Toast.LENGTH_SHORT).show()

            }


            else if(password.length<6){
                Toast.makeText(this,"Password length should be atleast 6 character", Toast.LENGTH_SHORT).show()
            }
            else if(!password.equals(compassword)){
                Toast.makeText(this,"Password does not match", Toast.LENGTH_SHORT).show()
            }
           else
            signUp(email,password)
        }
    }

    private fun isValidString(str: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }


    private fun signUp(email: String, password: String) {

        mauth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    addUserToDatabase(email,mauth.currentUser?.uid)
                    val i = Intent(this,VerifyEmail::class.java)
                    i.putExtra("keyy",email)
                    finish()
                    startActivity(i);

                } else {
                    Toast.makeText(this,"Some error occured", Toast.LENGTH_SHORT).show()

                }
            }

    }

//    private fun addUserToDatabase(email: String, uid: String?) {
//     mDbRef = Firebase.database.reference
//     mDbRef.child
//    }
}