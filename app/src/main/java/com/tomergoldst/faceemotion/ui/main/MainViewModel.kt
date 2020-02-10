package com.tomergoldst.faceemotion.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomergoldst.faceemotion.domain.FaceService
import com.tomergoldst.faceemotion.extentions.cropFaceRect
import com.tomergoldst.faceemotion.extentions.resolveEmotion
import com.tomergoldst.faceemotion.model.Face
import com.tomergoldst.faceemotion.model.FaceAttributeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class MainViewModel(private val faceService: FaceService) : ViewModel() {

    /**
     * Detected face
     */
    private val _face: MutableLiveData<Face> = MutableLiveData()
    val face: LiveData<Face>
        get() = _face

    /**
     * Face Detection error
     */
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String>
        get() = _error

    /**
     * Call detect to start the process of face detection
     */
    fun detect(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            detectAndCrop(bitmap)
        }
    }

    private fun detectAndCrop(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        faceService.detect(
            inputStream,
            arrayOf(FaceAttributeType.EMOTION),
            { faces ->
                if (faces.isEmpty()) {
                    _error.postValue(String.format("Detection failed"))
                    return@detect
                }

                // For this app we only check for the first detected face
                val newBitmap = bitmap.cropFaceRect(faces[0].faceRectangle)
                val emotion = faces[0].resolveEmotion()

                // update results
                _face.postValue(Face(newBitmap, emotion))

                bitmap.recycle()
            }, { errorMsg ->
                _error.postValue(String.format("Detection failed: %s", errorMsg))
            })

    }

}
