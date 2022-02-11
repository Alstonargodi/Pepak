package com.example.lepepak.View

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.lepepak.Model.Firebasedb
import com.example.lepepak.R
import com.example.lepepak.Util.Classficer
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_draw.*
import kotlinx.android.synthetic.main.fragment_draw.view.*


class Draw : Fragment() {
    lateinit var dbrefrence : DatabaseReference
    lateinit var latinfb : ArrayList<Firebasedb>

    private var textview = StringBuilder()
    private var word : String = ""
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_draw, container, false)

        var digitclasficer = Classficer(requireContext())

        latinfb = arrayListOf()

        view.draw_lay.setStrokeWidth(20.0f)
        view.draw_lay.setColor(Color.BLACK)

        view.btndraw_reset.setOnClickListener {
            view.draw_lay.clearCanvas()
        }

        view.draw_lay.setOnTouchListener { view, motionEvent ->
            view.draw_lay.onTouchEvent(motionEvent)


            btndraw_pred.setOnClickListener {
                var draw = draw_lay.getBitmap()
                if ((draw != null) && (digitclasficer.isinitialize)){
                    digitclasficer
                        .classifyAsync(draw)
                        .addOnSuccessListener {
                            tv_resultdraw.setText(it)
                            Log.d("result",it)
                            word = it
                            dbrefrence = FirebaseDatabase.getInstance().getReference("data")
                            dbrefrence.child(word).addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()){
                                        for(i in snapshot.children){

                                            val desc = snapshot.child("desc").value.toString()
                                            tvresult_desc.setText(desc)

                                            val link = snapshot.child("link").value.toString()
                                            Glide.with(requireContext())
                                                .asBitmap()
                                                .load(link)
                                                .into(img_icon)
                                        }

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("error","fetch error")
                                }

                            })
                        }
                }

                digitclasficer
                    .init()
                    .addOnFailureListener { Log.d("fail","fail") }
            }

            true
        }

        view.btndraw_add.setOnClickListener {
            textview.append(word)
            view.etdraw_result.setText(textview)

        }


        return view
    }

}