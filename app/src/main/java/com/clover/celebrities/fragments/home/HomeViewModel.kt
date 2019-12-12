package com.clover.celebrities.fragments.home

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amazonaws.services.rekognition.model.Celebrity
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesResult
import com.clover.celebrities.services.AmazonService
import com.clover.celebrities.wikiUtils.extractors.WikiBDayExtractor
import com.clover.celebrities.wikiUtils.extractors.WikiImageExtractor
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import com.clover.celebrities.MyCelebrityModel


class HomeViewModel : ViewModel() {

    private val celebrityLiveData = MutableLiveData<List<MyCelebrityModel>>()
    private val progressBarVisibility = MutableLiveData<Int>()
    private val unrecognizedCelebrities = MutableLiveData<Int>()

    fun getCelebritiesFromImage(bitmap: Bitmap) {
        CoroutineScope(IO).launch { getCelebritiesFrom(bitmap) }
    }

    fun subscribeToRecognizedCelebrities() = celebrityLiveData as LiveData<List<MyCelebrityModel>>

    fun subscribeToProgressBarVisibility() = progressBarVisibility as LiveData<Int>

    fun subscribeToUnrecognizedCelebrities() = unrecognizedCelebrities as LiveData<Int>


    private suspend fun getCelebritiesFrom(image: Bitmap)  {
        updateProgressBarVisibility(View.VISIBLE)

        val byteBuffer = getByteBufferFromImage(image)
        val result = getRecognizeCelebritiesWith(byteBuffer)
        val recognizedCelebrities = getCelebritiesInfo(result.celebrityFaces)

        if(recognizedCelebrities.isNotEmpty()) {
            updateUnrecognizedCelebrities(result.unrecognizedFaces.size)
        }
        updateCelebrityLiveData(recognizedCelebrities)
        updateProgressBarVisibility(View.GONE)
    }

    private suspend fun getRecognizeCelebritiesWith(byteBuffer: ByteBuffer): RecognizeCelebritiesResult {
        val request = RecognizeCelebritiesRequest().withImage(Image().withBytes(byteBuffer))
        return withContext(Dispatchers.Default) {
            AmazonService.rekognitionClient.recognizeCelebrities(request)
        }
    }

    private suspend fun getByteBufferFromImage(image: Bitmap): ByteBuffer
            = withContext(Dispatchers.Default) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        ByteBuffer.wrap(stream.toByteArray())
    }

    private suspend fun getCelebritiesInfo(celebrityFaces: List<Celebrity>): List<MyCelebrityModel> =
        coroutineScope {
            val tasks = arrayListOf<Deferred<MyCelebrityModel>>()
            for (celebrity in celebrityFaces) {
                val task = async { createCelebrity(celebrity) }
                tasks.add(task)
            }

            val celebrityList = arrayListOf<MyCelebrityModel>()
            for (task in tasks) {
                celebrityList.add(task.await())
            }

            celebrityList
        }

    private suspend fun createCelebrity(celebrity: Celebrity): MyCelebrityModel {
        val name = celebrity.name
        val wikiImageExtractor = WikiImageExtractor(name)
        val wikiBDayExtractor = WikiBDayExtractor(name)
        return MyCelebrityModel(
            name,
            wikiBDayExtractor.getCelebrityInfo(),
            wikiImageExtractor.getCelebrityInfo(),
            wikiImageExtractor.getWikiUrlString()
        )
    }

    private fun updateCelebrityLiveData(celebrities: List<MyCelebrityModel>) =
        GlobalScope.launch(Main) {
            celebrityLiveData.value = celebrities
        }

    private fun updateProgressBarVisibility(visibility: Int) = GlobalScope.launch(Main) {
        progressBarVisibility.value = visibility
    }

    private fun updateUnrecognizedCelebrities(unrecognizedNumber: Int) = GlobalScope.launch(Main) {
        unrecognizedCelebrities.value = unrecognizedNumber
    }

}