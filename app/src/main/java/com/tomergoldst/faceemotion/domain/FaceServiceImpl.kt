package com.tomergoldst.faceemotion.domain

import com.tomergoldst.faceemotion.api.model.Face
import com.tomergoldst.faceemotion.api.service.FaceDetectionService
import com.tomergoldst.faceemotion.model.FaceAttributeType
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.util.*


class FaceServiceImpl(
    private val apiKey: String,
    private val faceDetectionService: FaceDetectionService
) : FaceService {

    override fun detect(
        inputStream: InputStream,
        faceAttributes: Array<FaceAttributeType>,
        success: (List<Face>) -> Unit,
        error: (error: String) -> Unit
    ) {
        val requestBody = RequestBody
            .create(MediaType.parse("application/octet-stream"), inputStream.readBytes())
        faceDetectionService.detect(
            apiKey,
            returnFaceId = false,
            returnFaceLandmarks = false,
            returnFaceAttributes = faceAttributes.joinToString().toLowerCase(Locale.US),
            image = requestBody
        ).enqueue(object : Callback<List<Face>> {
            override fun onFailure(call: Call<List<Face>>, t: Throwable) {
                error(t.message?.let { it } ?: "Unknown error")
            }

            override fun onResponse(
                call: Call<List<Face>>,
                response: Response<List<Face>>
            ) {
                if (response.isSuccessful) {
                    success(response.body()?.let { it } ?: emptyList())
                } else {
                    error(response.message())
                }
            }
        })

    }
}