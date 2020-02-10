package com.tomergoldst.faceemotion.api.service

import com.tomergoldst.faceemotion.api.model.Face
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * API communication setup via Retrofit.
 */
interface FaceDetectionService {

    @POST("detect")
    fun detect(
        @Header("Ocp-Apim-Subscription-Key") key: String,
        @Query("returnFaceId") returnFaceId: Boolean = false,
        @Query("returnFaceLandmarks") returnFaceLandmarks: Boolean = false,
        @Query("returnFaceAttributes") returnFaceAttributes: String = setOf("emotion").joinToString(),
        @Body image: RequestBody?
    ): Call<List<Face>>

    companion object {
        private const val BASE_URL = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/"

        fun create(): FaceDetectionService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FaceDetectionService::class.java)
        }
    }
}