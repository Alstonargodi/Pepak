package com.example.lepepak.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lepepak.Model.Firebasedb
import com.example.lepepak.R
import com.example.lepepak.Util.adapter.Rvbookadapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_book.*
import kotlinx.android.synthetic.main.fragment_book.view.*


class Book : Fragment() {
    lateinit var dbrefrence : DatabaseReference

    lateinit var datalist : ArrayList<Firebasedb>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_book, container, false)


        val recview = view.recview_book
        val adapterrec = Rvbookadapter()
        recview.adapter = adapterrec
        recview.layoutManager = LinearLayoutManager(requireContext())

        datalist = arrayListOf()
        dbrefrence = FirebaseDatabase.getInstance().getReference("data")
        dbrefrence.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        val data = i.getValue(Firebasedb::class.java)
                        datalist.add(data!!)

                        adapterrec.setdata(datalist)
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