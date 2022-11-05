package com.example.lepepak.presentasion.fragment.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.lepepak.helpers.MainPrediction
import com.example.lepepak.databinding.FragmentBottomDetailListDialogBinding
import com.example.lepepak.databinding.FragmentDetailBottomListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class DetailBottomDialogFragment : Fragment() {

    private var _binding: FragmentBottomDetailListDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBottomDetailListDialogBinding.inflate(inflater, container, false)

        val letterClassification = MainPrediction(requireContext())

        val name = DetailBottomDialogFragmentArgs.fromBundle(requireArguments()).nama
        val description = DetailBottomDialogFragmentArgs.fromBundle(requireArguments()).desc
        val imageUrl = DetailBottomDialogFragmentArgs.fromBundle(requireArguments()).link

        //detail
        binding.tvdetailNama.text = name
        binding.tvdetailDesc.text = description

        Glide.with(requireContext())
            .asBitmap()
            .load(imageUrl)
            .into(binding.imgDetail)

        //draw area
        binding.laydetailDraw.setStrokeWidth(20.0F)
        binding.laydetailDraw.setColor(Color.BLACK)

        binding.btnClear.setOnClickListener {
            binding.laydetailDraw.clearCanvas()
        }

        binding.laydetailDraw.setOnTouchListener { _, motionEvent ->
            binding.laydetailDraw.onTouchEvent(motionEvent)
            binding.btnPredict.setOnClickListener {
                val bitmapImage = binding.laydetailDraw.getBitmap()
                if (letterClassification.isInitialize){
                    letterClassification
                        .classifyAsync(bitmapImage)
                        .addOnSuccessListener {
                            binding.tvbottomResult.text = it
                        }
                }
                letterClassification
                    .init()
                    .addOnFailureListener { Log.d("failed to classicfy",it.toString()) }
            }
            true
        }
        return binding.root
    }

}