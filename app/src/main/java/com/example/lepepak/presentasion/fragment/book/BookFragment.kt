package com.example.lepepak.presentasion.fragment.book

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lepepak.model.firebase.JavaneseDBResponse
import com.example.lepepak.R
import com.example.lepepak.presentasion.fragment.book.adapter.BookRecyclerViewAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_book.view.*


class BookFragment : Fragment() {
    private lateinit var firebaseReference : DatabaseReference
    private lateinit var dataList : ArrayList<JavaneseDBResponse>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_book, container, false)

        val recyclerView = view.recview_book
        val recyclerAdapter = BookRecyclerViewAdapter()
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dataList = arrayListOf()
        firebaseReference = FirebaseDatabase.getInstance().getReference("data")
        firebaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        val data = i.getValue(JavaneseDBResponse::class.java)
                        dataList.add(data!!)
                        recyclerAdapter.setdata(dataList)
                        Log.d("data", data.bentuk)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error","fetch data error $error")
            }
        })

        return view
    }
}