package com.example.mtgscannerproject

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mtgscannerproject.ui.theme.MTGScannerProjectTheme

class CameraActivity : ComponentActivity() {

    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MTGScannerProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CameraScreen( modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun CameraScreen(modifier: Modifier = Modifier) {
        // Implement CameraX functionality here to show camera preview
        // Use ML Kit for text recognition from the camera feed
        val previewView = PreviewView(this)

        AndroidView(
            factory = { previewView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }},
            modifier = modifier
        )

        // Start the camera when the Composable is launched
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

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Bind the camera to the lifecycle
            val useCaseGroup = UseCaseGroup.Builder().addUseCase(previewUseCase).build()
            cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
        }, ContextCompat.getMainExecutor(this))
    }

    // Additional methods for handling camera and ML Kit functionality...
}
