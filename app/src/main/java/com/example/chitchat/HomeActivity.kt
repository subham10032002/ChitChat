package com.example.chitchat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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

         setSupportActionBar(toolbar)
        mauth = Firebase.auth


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signOut ->{
                mauth.signOut()
                finish()
                startActivity(Intent(this,LoginActivity::class.java))
                return true
            }
        }
        return true
    }


}