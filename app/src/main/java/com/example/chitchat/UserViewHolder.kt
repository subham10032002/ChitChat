package com.example.chitchat

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(userModel: UserModel) {
        with(itemView) {
            countTv.isVisible = false
            timeTV.isVisible = false

            titleTV.text = userModel.name
            subTitleTV.text = userModel.status

            Picasso.get()
                .load(userModel.thumbImage)
                .placeholder(R.drawable.img_1)
                .error(R.drawable.img_1)
                .into(userImgView)


        }
    }
}