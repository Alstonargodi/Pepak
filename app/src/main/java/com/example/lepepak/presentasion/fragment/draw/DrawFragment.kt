package com.example.lepepak.presentasion.fragment.draw

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.lepepak.model.firebase.JavaneseDBResponse
import com.example.lepepak.databinding.FragmentDrawBinding
import com.example.lepepak.helpers.SecondPrediction
import com.google.firebase.database.*


class DrawFragment : Fragment() {

    private var _binding: FragmentDrawBinding? = null
    private val binding get() = _binding!!

    lateinit var dbrefrence : DatabaseReference
    private lateinit var latinWord : ArrayList<JavaneseDBResponse>

    private var wordBuilder = StringBuilder()
    private var word : String = ""
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentDrawBinding.inflate(
            inflater,container,false
        )

        val classsification = SecondPrediction(requireContext())

        latinWord = arrayListOf()

        binding.drawLay.apply {
            setStrokeWidth(20.0f)
            setColor(Color.BLACK)
        }

        binding.btndrawReset.setOnClickListener {
            binding.drawLay.clearCanvas()
        }

        binding.drawLay.setOnTouchListener { view, motionEvent ->
            binding.drawLay.onTouchEvent(motionEvent)

            binding.btndrawPred.setOnClickListener {
                val bitmapDraw = binding.drawLay.getBitmap()

                classsification
                    .initPrediction(bitmapDraw)
                    .addOnSuccessListener {
                        binding.tvResultdraw.text = it.character
                        showCharacterDescription(it.character)
                    }
                    .addOnFailureListener {
                        binding.tvResultdraw.text = it.message.toString()
                    }
            }

            true
        }
        binding.btndrawAdd.setOnClickListener {
            binding.textView.append(word)
            binding.etdrawResult.text = wordBuilder
        }
        return binding.root
    }

    private fun showCharacterDescription(character : String){
        dbrefrence = FirebaseDatabase.getInstance().getReference("data")
        dbrefrence.child(character).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(i in snapshot.children){
                        val data = i.getValue(JavaneseDBResponse::class.java)
                        if (data != null) {
                            Log.d("detail data",data.toString())
                            binding.tvresultDesc.text = data.bentuk
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(data.link)
                                .into(binding.imgIcon)
                        }
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("DrawFragment","fetch error")
            }
        })
    }
}