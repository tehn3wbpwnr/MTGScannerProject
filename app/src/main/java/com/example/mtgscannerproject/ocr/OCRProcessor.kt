package com.example.mtgscannerproject.ocr

import android.util.Log
import com.google.mlkit.vision.text.Text

class OCRProcessor {

    private var lastDetectedText = ""
    private var lastConfirmedDetection = ""
    private var stableCount = 0

    fun extractText(visionText: Text): String? {

        if (visionText.textBlocks.isEmpty()) {
            return null
        }

        // Sort blocks by Y position (top first)
        val sortedBlocks = visionText.textBlocks.sortedBy {
            it.boundingBox?.top ?: Int.MAX_VALUE
        }

        // Assume top-most block is card name
        val cardName = sortedBlocks.first().text.trim()

        Log.d("CARD_NAME", cardName)

        return handleDetection(cardName)
    }

    private fun handleDetection(text: String): String? {

        if (text == lastDetectedText) {
            stableCount++
        } else {
            stableCount = 1
            lastDetectedText = text
        }

        Log.d("OCR_STABLE", "$text : $stableCount")

        if (stableCount >= 3 && text != lastConfirmedDetection) {

            Log.d("OCR_STABLE", "Stable detection: $text")

            stableCount = 0
            lastConfirmedDetection = text

            return text
        }

        return null
    }
}