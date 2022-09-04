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
import com.bumptech.glide.Glide
import com.example.lepepak.helpers.MainPrediction
import com.example.lepepak.databinding.FragmentBottomDetailListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class DetailBottomDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomDetailListDialogBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomDetailListDialogBinding.inflate(inflater, container, false)

        val letterClassification = MainPrediction(requireContext())

        val name = arguments?.getString("nama")
        val description = arguments?.getString("desc")
        val imageUrl = arguments?.getString("link")

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

    //full dialog config
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { height ->
                val behaviour = BottomSheetBehavior.from(height)
                setupFullHeight(height)
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