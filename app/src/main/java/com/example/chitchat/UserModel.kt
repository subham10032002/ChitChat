package com.example.chitchat

import com.google.firebase.firestore.FieldValue

data class UserModel(
    val name: String,
    val email: String,
    val imageUrl: String,
    val thumbImage: String,
    val uid: String,
    val deviceToken: String,
    val status: String,
    val onlineStatus: String
    ){
//    making empty constructor whenever we are making data class for firebase
    constructor() : this("","","","","",
                 "","", "")

    constructor(name: String, email: String?, imageUrl: String, thumbImage: String, uid: String) : this(
        name, email.toString(),imageUrl,thumbImage,uid,
        "","Hey there I'm using ChitChat","")
}