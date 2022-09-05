package com.example.lepepak.presentasion.fragment.camera

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lepepak.R
import com.example.lepepak.databinding.FragmentCameraBinding
import com.example.lepepak.helpers.SecondPrediction
import java.security.Permissions

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var secondPrediction: SecondPrediction
    private lateinit var buffer : Bitmap
    private var imageAnalyzer : ImageAnalysis? = null

    private val launchIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){

        }
    }

    private fun permissionGranted() = required_permission.all {
        ContextCompat.checkSelfPermission(requireContext(),it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!permissionGranted()){
                Toast.makeText(
                    requireContext(),"no permit",Toast.LENGTH_SHORT
                ).show()
                requireActivity().finishAffinity()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCameraBinding.inflate(inflater,container,false)

        if (!permissionGranted()){
            ActivityCompat.requestPermissions(
                requireActivity(), required_permission, REQUEST_CODE_PERMISSIONS
            )
        }
        startCamera()
        return binding.root
    }

    private fun startCamera(){
        val cameraProvider = ProcessCameraProvider.getInstance(requireContext())
        cameraProvider.addListener({
            val provider : ProcessCameraProvider = cameraProvider.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(
                        binding.previewView.surfaceProvider
                    )
                }

            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
                )
            }catch (e : Exception){
                Toast.makeText(
                    context,
                    e.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(camera_fragment,e.message.toString())
            }
        },ContextCompat.getMainExecutor(requireContext()))
    }


    companion object{
        private val required_permission = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val camera_fragment = "camera_fragment"
    }
}