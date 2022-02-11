package com.example.lepepak

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lepepak.Util.Classakurasi
import com.example.lepepak.Util.Classficer
import com.example.lepepak.databinding.FragmentBottomDetailListDialogBinding
import com.example.lepepak.databinding.FragmentDetailBottomListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class Detail_bottom : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomDetailListDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBottomDetailListDialogBinding.inflate(inflater, container, false)

        var digitclasficer = Classakurasi(requireContext())

        val nama = arguments?.getString("nama")
        val desc = arguments?.getString("desc")
        val link = arguments?.getString("link")

        //detail
        binding.tvdetailNama.setText(nama)
        binding.tvdetailDesc.setText(desc)

        Glide.with(requireContext())
            .asBitmap()
            .load(link)
            .into(binding.imgDetail)

        //draw area
        binding.laydetailDraw.setStrokeWidth(20.0F)
        binding.laydetailDraw.setColor(Color.BLACK)

        binding.btnClear.setOnClickListener {
            binding.laydetailDraw.clearCanvas()
        }

        binding.laydetailDraw.setOnTouchListener { view, motionEvent ->
            binding.laydetailDraw.onTouchEvent(motionEvent)


            binding.btnPredict.setOnClickListener {
                var bit = binding.laydetailDraw.getBitmap()
                if ((bit != null) && (digitclasficer.isinitialize)){
                    digitclasficer
                        .classifyAsync(bit)
                        .addOnSuccessListener {
                            Log.d("hasil",it)
                            binding.tvbottomResult.setText(it)
                        }
                }
                digitclasficer
                    .init()
                    .addOnFailureListener { Log.d("failed to classicfy",it.toString()) }
            }
            true
        }


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