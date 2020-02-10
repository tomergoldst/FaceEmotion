package com.tomergoldst.faceemotion.extentions

import android.graphics.Bitmap
import com.tomergoldst.faceemotion.api.model.Face
import com.tomergoldst.faceemotion.api.model.FaceRectangle
import com.tomergoldst.faceemotion.model.Emotion

fun Face.resolveEmotion(): Emotion {
    var strongestEmotion: Emotion = Emotion.UNKNOWN

    val emotionAttributes = faceAttributes.emotion
    var maxScore = 0.0

    if (emotionAttributes.anger > maxScore) {
        maxScore = emotionAttributes.anger
        strongestEmotion = Emotion.ANGER
    }
    if (emotionAttributes.contempt > maxScore) {
        maxScore = emotionAttributes.contempt
        strongestEmotion = Emotion.CONTEMPT
    }
    if (emotionAttributes.disgust > maxScore) {
        maxScore = emotionAttributes.disgust
        strongestEmotion = Emotion.DISGUST
    }
    if (emotionAttributes.fear > maxScore) {
        maxScore = emotionAttributes.fear
        strongestEmotion = Emotion.FEAR
    }
    if (emotionAttributes.happiness > maxScore) {
        maxScore = emotionAttributes.happiness
        strongestEmotion = Emotion.HAPPINESS
    }
    if (emotionAttributes.neutral > maxScore) {
        maxScore = emotionAttributes.neutral
        strongestEmotion = Emotion.NEUTRAL
    }
    if (emotionAttributes.sadness > maxScore) {
        maxScore = emotionAttributes.sadness
        strongestEmotion = Emotion.SADNESS
    }
    if (emotionAttributes.surprise > maxScore) {
        strongestEmotion = Emotion.SURPRISE
    }
    return strongestEmotion
}

fun Bitmap.cropFaceRect(faceRectangle: FaceRectangle): Bitmap {
    return Bitmap.createBitmap(
        this,
        faceRectangle.left,
        faceRectangle.top,
        faceRectangle.width,
        faceRectangle.height
    )
}
