package com.example.lepepak.presentasion.fragment.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lepepak.databinding.FragmentCameraBinding
import com.example.lepepak.helpers.SecondPrediction
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import kotlinx.coroutines.Runnable

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var secondPrediction: SecondPrediction
    private lateinit var buffer : Bitmap
    private var imageAnalyzer : ImageAnalysis? = null
    private lateinit var objectDetector : ObjectDetector

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
        cameraProvider.addListener(
            Runnable {
                val provider : ProcessCameraProvider = cameraProvider.get()
                bindPreview(provider)
            },ContextCompat.getMainExecutor(requireContext()))

        val localModel = LocalModel.Builder()
            .setAssetFilePath("aksaraModelTeach.tflite")
            .build()

        val customObjectDetector = CustomObjectDetectorOptions.Builder(localModel)
            .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .setClassificationConfidenceThreshold(0.5f)
            .setMaxPerObjectLabelCount(3)
            .build()

        objectDetector = ObjectDetection.getClient(customObjectDetector)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider){
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(
                    binding.previewView.surfaceProvider
                )
            }

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280,720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext())){ imageproxy ->

            val image = imageproxy.image
            if ( image != null){
                val media = InputImage.fromMediaImage(image,imageproxy.imageInfo.rotationDegrees)
                objectDetector.process(media)
                    .addOnSuccessListener { result ->
                        for(i in result){
                           val detectBox = DrawBox(
                               requireContext(),
                               i.boundingBox,
                               i.labels.firstOrNull()?.text ?: "undefined"
                           )
                            Log.d("detect aksara",i.labels.toString())
                            binding.previewView.addView(detectBox)
                            binding.camera.addView(detectBox)
                            showToast(i.labels.toString())

                        }
                    }
                    .addOnFailureListener {
                        Log.d("detect aksara",it.toString())
                        showToast(it.toString())
                    }
            }else{
                Log.d("detect aksara","null")
                showToast("null")
            }

        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )
        }catch (e : Exception){
            Toast.makeText(
                context,
                e.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
            Log.d(camera_fragment,e.message.toString())
        }
    }

    private fun showToast(text : String){
        Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show()
    }

    companion object{
        private val required_permission = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val camera_fragment = "camera_fragment"
    }
}