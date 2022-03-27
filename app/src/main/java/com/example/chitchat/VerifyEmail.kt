package com.example.chitchat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_verify_email.*


class VerifyEmail : AppCompatActivity() {

    private lateinit var mauth : FirebaseAuth
    var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        val bundle: Bundle? = intent.extras
        email = bundle?.getString("keyy")
        mauth = Firebase.auth
        email_data.text= email;
        btn_verify_email.setOnClickListener {
            sendEmailVerification()
        }



    }

    private fun sendEmailVerification() {
        //get instance of firebase auth
//        val firebaseAuth = FirebaseAuth.getInstance()
        //get current user
        val firebaseUser = mauth.currentUser

        //send email verification



        firebaseUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification mail sent " , Toast.LENGTH_SHORT).show()
                   btn_verify_done.visibility=View.VISIBLE
                }
                else
                {
                    Toast.makeText(this, "Failed to send verification mail " , Toast.LENGTH_SHORT).show()

                }
            }


        btn_verify_done.setOnClickListener {

            if (firebaseUser.isEmailVerified) {
                Toast.makeText(this, "User is verified", Toast.LENGTH_SHORT).show()
                val i = Intent(this,ProfileActivity::class.java)
                i.putExtra("emaill",email)
                startActivity(i)
            } else {
                Toast.makeText(this, "User is verified", Toast.LENGTH_SHORT).show()
                val i = Intent(this,ProfileActivity::class.java)
                i.putExtra("emaill",email)
                startActivity(i)
            }

        }

    }


}