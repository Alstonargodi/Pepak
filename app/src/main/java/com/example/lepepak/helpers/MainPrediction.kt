package com.example.lepepak.helpers

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import com.example.lepepak.model.LatinWord
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.jvm.Throws

class MainPrediction(val context: Context) {

    //tflite interpreter
    private var interpreter : Interpreter? = null
    var isInitialize = false

    //executor pada background
    private val executorService : ExecutorService = Executors.newCachedThreadPool()
    private var inputImgWidth : Int = 0
    private var inputImgHeight : Int = 0
    private var modelInputSize : Int = 0
    private var final : String = ""

    fun init(): Task<Void> {
        val task = TaskCompletionSource<Void>()
        executorService.execute {
            try{
                initInterpreter()
                task.setResult(null)
            }catch (e : IOException){
                task.setException(e)
            }
        }
        return task.task
    }

    @Throws(IOException::class)
    private fun initInterpreter(){
        //mengambil asset tf lite
        val mAssets = context.assets
        val mlModel = loadModel(mAssets,"aksaramodel28150.tflite")

        val interpreter = Interpreter(mlModel) //

        //membaca bentuk tensor
        val inputShape = interpreter.getInputTensor(0).shape()
        inputImgWidth = inputShape[1]
        inputImgHeight = inputShape[2]
        modelInputSize = FLOAT_TYPE_SIZE * inputImgWidth * inputImgHeight * PIXEL_SIZE
        //4 * inputwidthtensor * inputsizetensor * 1

        this.interpreter = interpreter
        isInitialize = true
    }

    @Throws(IOException::class)
    private fun loadModel(assetManager: AssetManager, filename: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename) //membuka file
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)

        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }



    private fun classification(bitmap: Bitmap): String{

        //preprocessing
        val resizeImage = Bitmap.createScaledBitmap(
            bitmap,
            inputImgWidth,
            inputImgHeight,
            true
        )

        val byteBuffer = conversitionBitmapToByteBuffer(resizeImage)

        val output = Array(1){
            FloatArray(OUTPUT_CLASSES_COUNT)
        }


        interpreter?.run(byteBuffer,output) //predict gambar dengan model

        val result = output[0]


        //get result
        val maxIndex = result.indices.maxByOrNull { result[it] } ?: -1
        val resultString = "result: %.2f".format(result[maxIndex])

        val resultDetect = LatinWord.aksara

        for (i in resultDetect.indices) {

            if (maxIndex < resultDetect.size) {
                val hasil = resultDetect[maxIndex]

                final = resultString
            } else {
                final = "cannot find index = $maxIndex"
            }

        }

        return final
    }



    fun classifyAsync(bitmap: Bitmap): Task<String> {
        val task = TaskCompletionSource<String>()
        executorService.execute {
            val result = classification(bitmap)

            task.setResult(result)

        }
        return task.task
    }



    private fun conversitionBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())



        val pixels = IntArray(inputImgWidth * inputImgHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            // Convert RGB to grayscale and normalize pixel value to [0..1].
            val normalizedPixelValue = (r + g + b) / 3.0f / 255.0f
            byteBuffer.putFloat(normalizedPixelValue)
        }
        return byteBuffer
    }


    companion object{
        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1
        private const val OUTPUT_CLASSES_COUNT = 20 //jumlah class
    }
}