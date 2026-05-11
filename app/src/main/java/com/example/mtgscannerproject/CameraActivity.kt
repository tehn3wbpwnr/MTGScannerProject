package com.example.mtgscannerproject

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mtgscannerproject.ui.theme.MTGScannerProjectTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.TimeUnit

class CameraActivity : ComponentActivity() {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageAnalyzer: ImageAnalysis
    private val recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private var lastProcessedTime = System.currentTimeMillis()
    private var lastDetectedText = ""
    private var lastConfirmedDetection = ""
    private var stableCount = 0
    private val detectedText = mutableStateOf("Scanning...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MTGScannerProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun CameraScreen(modifier: Modifier = Modifier) {

        val previewView = PreviewView(this)

        Box(modifier = modifier.fillMaxSize()) {

            AndroidView(
                factory = {
                    previewView.apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = detectedText.value,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }

        LaunchedEffect(Unit) {
            startCamera(previewView)
        }
    }

    private fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // Build the preview use case and set the surface provider
            val previewUseCase = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Set up ImageAnalysis use case
            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysisUseCase ->
                    analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            // Select the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Bind the camera lifecycle with both Preview and ImageAnalysis use cases
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                previewUseCase,
                imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastProcessedTime >= TimeUnit.SECONDS.toMillis(1)) {
            val mediaImage = imageProxy.image

            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                // Pass the image to ML Kit's text recognizer
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        extractText(visionText)
                    }
                    .addOnFailureListener { e ->
                        Log.e("CameraActivity", "Text recognition failed: ${e.message}", e)
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
            lastProcessedTime = currentTime
        } else {
            imageProxy.close()
        }
    }

    //This function runs the OCR to extract text and currently logs the blocks, lines, and elements
    private fun extractText(visionText: Text) {

        if (visionText.textBlocks.isEmpty()) {
            return
        }

        // Sort blocks by Y position (top of screen first)
        val sortedBlocks = visionText.textBlocks.sortedBy {
            it.boundingBox?.top ?: Int.MAX_VALUE
        }

        //for now assume top block is card name
        val cardName = sortedBlocks.first().text.trim()

        Log.d("CARD_NAME", cardName)

        handleDetection(cardName)
    }

    //This is an initial implementation of trying to only allow a stable text detection to trigger further action
    private fun handleDetection(text: String){
        if(text == lastDetectedText){
            stableCount++
        } else {
            stableCount = 1
            lastDetectedText = text
        }

        if (stableCount >= 3 && text != lastConfirmedDetection) {
            Log.d("OCR_STABLE", "stable detection: $text")
            stableCount = 0;
            //TODO call further actions here
            detectedText.value = text
            lastConfirmedDetection = text
        }
    }

    //Try and regex match the patterns of set codes/collector numbers probably move to utils later
    private fun parsePossibleCardData(setCode: String, cardNum: String) {

        val setCodeRegex = Regex("\\b[A-Z0-9]{3}\\b")
        val collectorRegex = Regex("\\b\\d{1,3}[a-zA-Z]?\\b")

        val setMatches = setCodeRegex.findAll(setCode)
        val collectorMatches = collectorRegex.findAll(cardNum)

        for (match in setMatches) {
            Log.d("SET_CODE", match.value)
        }

        for (match in collectorMatches) {
            Log.d("COLLECTOR_NUM", match.value)
        }
    }
}

