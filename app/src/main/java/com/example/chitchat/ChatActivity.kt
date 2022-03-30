package com.example.chitchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*


const val UID = "uid"
const val NAME = "name"
const val IMAGE = "photo"
class ChatActivity : AppCompatActivity() {

    private val friendId: String? by lazy {
        intent.getStringExtra(UID)
    }

    private val friendName: String? by lazy {
        intent.getStringExtra(NAME)
    }
    private val friendImage: String? by lazy {
        intent.getStringExtra(IMAGE)
    }
    private val mCurrentUid: String? by lazy {
        FirebaseAuth.getInstance().uid!!
    }
    private val db : FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    lateinit var currentUser : UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()


        mCurrentUid?.let {
            FirebaseFirestore.getInstance().collection("users").document(it).get()
                .addOnSuccessListener {
                    currentUser = it.toObject(UserModel::class.java)!!
                }
        }

        FirebaseFirestore.getInstance().collection("users").document(mCurrentUid!!).get()
            .addOnSuccessListener {
                currentUser = it.toObject(UserModel::class.java)!!
            }
        nameTv.text = friendName
        Picasso.get().load(friendImage).into(userImgView)


    }
}