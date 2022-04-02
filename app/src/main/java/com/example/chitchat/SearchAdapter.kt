package com.example.chitchat

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.list_item.view.*


class SearchAdapter( @NonNull options: FirestoreRecyclerOptions<UserModel>
) :
    FirestoreRecyclerAdapter<UserModel, SearchAdapter.UserrViewHolder>(options) {


    @NonNull
    override fun onBindViewHolder(
        holder: UserrViewHolder,
        position: Int,
        model: UserModel
    ) {
        holder.name.setText(model.name);

    }
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): UserrViewHolder {
        return UserrViewHolder(LayoutInflater.from(parent.context).inflate(com.example.chitchat.R.layout.list_item,parent,false))

    }

     class UserrViewHolder( @NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var name: TextView

        init {

            name = itemView.findViewById(com.example.chitchat.R.id.titleTV) as TextView

        }
    }


}


