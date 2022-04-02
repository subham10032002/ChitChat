package com.example.chitchat

import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class ChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item:Inbox, onClick: (name:String, photo: String , id:String)-> Unit)=
        with(itemView) {
            emailID.isVisible = false
            countTv.isVisible = item.count >0
            countTv.text = item.count.toString()
            timeTV.text = item.time.formatAsListItem(context)

            titleTV.text = item.name
            subTitleTV.text = item.msg
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.img_1)
                .error(R.drawable.img_1)
                .into(userImgView)
            setOnClickListener{
                onClick.invoke(item.name,item.image,item.from)
            }

        }

}