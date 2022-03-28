package com.example.chitchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var mauth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mauth = Firebase.auth

        supportActionBar?.hide()

        viewPager.adapter = ScreenSliderAdapter(this)
//        by clicking on the fragments we can chanfe the tab
        TabLayoutMediator(tabs,viewPager,
        TabLayoutMediator.TabConfigurationStrategy{ tab: TabLayout.Tab, i: Int ->
             when(i){
                 0 -> tab.text = "CHATS"
                 1 -> tab.text = "PEOPLE"
             }
        }
            ).attach()









    }
}