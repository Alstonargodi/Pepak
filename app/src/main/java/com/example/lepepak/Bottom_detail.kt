package com.example.lepepak

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lepepak.Util.Classficer
import com.example.lepepak.databinding.FragmentBottomDetailListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class Bottom_detail : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomDetailListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBottomDetailListDialogBinding.inflate(inflater, container, false)
//
//        var digitclasficer = Classficer(requireContext())
//
//        val nama = arguments?.getString("nama")
//        val desc = arguments?.getString("desc")
//        val link = arguments?.getString("link")
//
//        binding.laydetailDraw.setStrokeWidth(20.0F)
//        binding.laydetailDraw.setColor(Color.BLACK)
//
//
//       binding.laydetailDraw.setOnTouchListener { view, motionEvent ->
//            binding.laydetailDraw.onTouchEvent(motionEvent)
//
//
//            binding.btnPredict.setOnClickListener {
//                var draw = binding.laydetailDraw.getBitmap()
//                if ((draw != null) && (digitclasficer.isinitialize)){
//                    digitclasficer
//                        .classifyAsync(draw)
//                        .addOnSuccessListener {
//                            binding.tvbottomResult.setText(it)
//                            Log.d("result",it)
//                            word = it
//                            dbrefrence = FirebaseDatabase.getInstance().getReference("data")
//                            dbrefrence.child(word).addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    if (snapshot.exists()){
//                                        for(i in snapshot.children){
//
//                                            val desc = snapshot.child("desc").value.toString()
//                                            tvresult_desc.setText(desc)
//
//                                            val link = snapshot.child("link").value.toString()
//                                            Glide.with(requireContext())
//                                                .asBitmap()
//                                                .load(link)
//                                                .into(img_icon)
//                                        }
//
//                                    }
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                    Log.d("error","fetch error")
//                                }
//
//                            })
//                        }
//                }
//
//                digitclasficer
//                    .init()
//                    .addOnFailureListener { Log.d("fail","fail") }
//            }
//
//            true
//        }
//
//
//        binding.tvdetailNama.setText(nama)
//        binding.tvdetailDesc.setText(desc)
//
//        Glide.with(requireContext())
//            .asBitmap()
//            .load(link)
//            .into(binding.imgDetail)


        return binding.root
    }























    //full dialog config
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


}