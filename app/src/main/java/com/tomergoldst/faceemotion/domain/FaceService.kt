package com.tomergoldst.faceemotion.domain

import com.tomergoldst.faceemotion.api.model.Face
import com.tomergoldst.faceemotion.model.FaceAttributeType
import java.io.InputStream

interface FaceService {

    fun detect(
        inputStream: InputStream,
        faceAttributes: Array<FaceAttributeType>,
        success: (List<Face>) -> Unit,
        error: (error: String) -> Unit
    )

}