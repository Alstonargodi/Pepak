package com.example.lepepak.helpers

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.lepepak.ml.AksaraModelTeach
import com.example.lepepak.model.PredictionResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SecondPrediction(val context: Context) {

    private val executorService : ExecutorService = Executors.newCachedThreadPool()

    fun prediction(bitmap: Bitmap): PredictionResult{
        val resizeBitmap = Bitmap.createScaledBitmap(
            bitmap,
            224,
            224,
            false
        )

        val imageBuffer = TensorImage.fromBitmap(resizeBitmap)

        val model = AksaraModelTeach.newInstance(context)
        val modelFeature = TensorBuffer.createFixedSize(
            intArrayOf(1, 224, 224, 3), DataType.UINT8
        )

        Log.d("main model Buffer",modelFeature.buffer.toString())
        Log.d("main image Buffer",imageBuffer.buffer.toString())

        modelFeature.loadBuffer(imageBuffer.buffer)

        val outputs = model.process(modelFeature)
        val predictOutputs = outputs.outputFeature0AsTensorBuffer


        val labelPredict = context.applicationContext.assets
            .open("labelmap.txt")
            .bufferedReader()
            .use {
                it.readText()
            }
        val resultOut = labelPredict.split("\n")
        val word = getMaxIndex(predictOutputs.intArray)
        model.close()

        return PredictionResult(
            word,
            resultOut[word]
        )
    }

    fun initPrediction(bitmap: Bitmap): Task<PredictionResult>{
        val task = TaskCompletionSource<PredictionResult>()
        executorService.execute {
            val result = prediction(bitmap)
            task.setResult(result)
        }
        return task.task
    }
    private fun getMaxIndex(array : IntArray?): Int{
        if (array == null || array.isEmpty()) return -1
        var large = 0
        for (i in 1 until array.size){
            if (array[i] > array[large]) large = i
        }
        return large
    }





}