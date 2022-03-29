package com.example.chitchat


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.fragment_chat.*


private const val DELETED_VIEW_TYPE = 1
private const val NORMAL_VIEW_TYPE = 2

class PeopleFragment : Fragment() {

    private lateinit var mAdapter: FirestorePagingAdapter<UserModel, RecyclerView.ViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val database by lazy {
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("name", Query.Direction.ASCENDING)
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewManager = LinearLayoutManager(requireContext())
        setupAdapter()
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private fun setupAdapter() {
        // Init Paging Configuration
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(10)
            .build()

        // Init Adapter Configuration
        val options = FirestorePagingOptions.Builder<UserModel>()
            .setLifecycleOwner(this)
            .setQuery(database, config, UserModel::class.java)
            .build()

        // Instantiate Paging Adapter
        mAdapter = object : FirestorePagingAdapter<UserModel, RecyclerView.ViewHolder>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return when(viewType) {
                    NORMAL_VIEW_TYPE -> {
                          UserViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false))

                    }
                    else ->
                       EmptyViewHolder(layoutInflater.inflate(R.layout.empty_view, parent, false))


                }

            }

            override fun onBindViewHolder(
                viewHolder: RecyclerView.ViewHolder,
                position: Int,
                model: UserModel
            ) {
                if(viewHolder is UserViewHolder) {
                    viewHolder.bind(userModel = model)
                }
                }


            override fun onError(e: Exception) {
                super.onError(e)
                e.message?.let { Log.e("MainActivity", it) }
            }



            override fun onLoadingStateChanged(state: LoadingState) {
                when (state) {
                    LoadingState.LOADING_INITIAL -> {
                    }

                    LoadingState.LOADING_MORE -> {
                    }

                    LoadingState.LOADED -> {
                    }

                    LoadingState.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            "Error Occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    LoadingState.FINISHED -> {
                    }
                }
            }

            override fun getItemViewType(position: Int): Int {
                val item = getItem(position)?.toObject(UserModel::class.java)

                return if(auth.uid == item!!.uid){
                    DELETED_VIEW_TYPE
                }
                else {
                    NORMAL_VIEW_TYPE
                }
            }

        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragRV.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = mAdapter
        }

    }

}