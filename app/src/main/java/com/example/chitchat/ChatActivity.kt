package com.example.chitchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*


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

    private val messages = mutableListOf<ChatEvent>()

    lateinit var chatAdapter: ChatAdapter
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
         chatAdapter = ChatAdapter(messages,mCurrentUid!!)
        msgRv.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }
        nameTv.text = friendName
        Picasso.get().load(friendImage).into(userImgView)
        listenToMessages()
        sendBtn.setOnClickListener{
            msgEdtv.text?.let{
                if(it.isNotEmpty()){
                    sendMessage(it.toString())
                    it.clear()
                }
            }
        }

    }

    private fun listenToMessages(){
        getMessages(friendId!!)
            .orderByKey()
            .addChildEventListener(object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val msg = snapshot.getValue(Message::class.java)!!
                    addMessage(msg)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun addMessage(msg: Message) {
         val eventBefore = messages.lastOrNull()

        if((eventBefore != null && !eventBefore.sentAt.isSameDayAs(msg.sentAt)) || eventBefore == null){
            messages.add(DateHeader(msg.sentAt, context = this))

        }
        messages.add(msg)
        chatAdapter.notifyItemInserted(messages.size -1)
        msgRv.scrollToPosition(messages.size -1)
    }

    private fun sendMessage(msg: String) {

        val id = getMessages(friendId!!).push().key // push function will make a unique key
        checkNotNull(id){
            "Cannot be null"
        }
        val msgMap = Message(msg,mCurrentUid!!,id)
        getMessages(friendId!!).child(id).setValue(msgMap).addOnSuccessListener {

        }
        updateLastMessage(msgMap)
    }

    private fun updateLastMessage(message: Message) {
          val inboxMap = Inbox(
              message.msg,
              friendId!!,
              friendName!!,
              friendImage!!,
              count=0
          )
        getInbox(mCurrentUid!!,friendId!!).setValue(inboxMap).addOnSuccessListener {
            getInbox(friendId!!,mCurrentUid!!).addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Inbox::class.java)

                    inboxMap.apply {
                        from = message.senderId
                        name = currentUser.name
                        image = currentUser.thumbImage
                        count=1
                    }
                    value?.let {
                        if(it.from == message.senderId){
                            inboxMap.count = value.count +1
                        }
                    }

                    getInbox(friendId!!, mCurrentUid!!).setValue(inboxMap)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    private fun markAsRead(){
        getInbox(friendId!!,mCurrentUid!!).child("count").setValue(0)
    }

    //    id for the messages
    private fun getId(friendId: String) : String {
        return if(friendId> mCurrentUid!!){
            mCurrentUid + friendId
        }
        else {
              friendId + mCurrentUid

        }
    }

    private fun getInbox(toUser : String , fromUser : String) =
        db.reference.child("chats/$toUser/$fromUser")

    private fun getMessages(friendId : String ) =
        db.reference.child("messages/${getId(friendId)}")
}