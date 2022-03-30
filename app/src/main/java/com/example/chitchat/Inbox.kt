package com.example.chitchat

import java.util.*

data class Inbox(
    val msg:String,
    val from:String,
    var name:String,
    var image:String,
    val time:Date = Date(),
    val count:Int

){
    constructor() : this("","","","",Date(),
        0)
}