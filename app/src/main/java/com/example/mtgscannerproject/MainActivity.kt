package com.example.mtgscannerproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mtgscannerproject.ui.theme.MTGScannerProjectTheme


class MainActivity : ComponentActivity() {

    //Activity Result Launcher for permissions
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Initialize the ActivityResultLauncher
        requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                scanCards()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required to use the camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setContent {
            MTGScannerProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayMenu(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onScanCardsClicked = { requestCameraPermission() }, // Pass the click handler
                        onViewScannedCardsClicked = { /* TODO: Handle view scanned cards */ },
                        onExitClicked = { /* TODO: Handle view scanned cards */ } // Exit the activity
                    )
                }
            }
        }
    }

    //Functional components below

    // Function to request camera permission using the ActivityResultLauncher
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                scanCards()
            }

            else -> {
                // Launch the permission request
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun scanCards() {
        /* TODO: Add scan cards functionality */
        Toast.makeText(this, "Scan Cards button clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

}

//Composable functions below

@Composable
fun DisplayMenu(
    modifier: Modifier = Modifier,
    onScanCardsClicked: () -> Unit,
    onViewScannedCardsClicked: () -> Unit,
    onExitClicked: () -> Unit
) {
    Column(modifier = modifier) {
        DisplayTitle(text = "Menu")
        DisplayButtons(
            onScanCardsClicked = onScanCardsClicked,
            onViewScannedCardsClicked = onViewScannedCardsClicked,
            onExitClicked = onExitClicked
        )
    }
}

@Composable
fun DisplayTitle(modifier: Modifier = Modifier, text: String) {
    Box(modifier = modifier
        .fillMaxWidth() // Fill the width of the screen
        .background(Color.Blue) // Set the background color
        .padding(16.dp) // Add padding to the box
    ) {
        Text(
            text = text,
            color = Color.White, // Set text color for contrast
            fontSize = 24.sp, // Set the font size
            modifier = Modifier.align(Alignment.Center) // Center the text in the box
        )
    }
}

@Composable
fun DisplayButtons(
    modifier: Modifier = Modifier,
    onScanCardsClicked: () -> Unit = {},
    onViewScannedCardsClicked: () -> Unit = {},
    onExitClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize() // Fill the available space
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Center the Column in the Box
                .padding(16.dp), // Padding around the Column
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onScanCardsClicked() },
                modifier = Modifier.fillMaxWidth(0.66f) // 2/3 of the width
            ) {
                Text(text = "Scan Cards")
            }

            Spacer(modifier = Modifier.height(12.dp)) // Space between buttons

            Button(
                onClick = { onViewScannedCardsClicked() },
                modifier = Modifier.fillMaxWidth(0.66f) // 2/3 of the width
            ) {
                Text(text = "View Scanned Cards")
            }

            Spacer(modifier = Modifier.height(12.dp)) // Space between buttons

            Button(
                onClick = { onExitClicked() },
                modifier = Modifier.fillMaxWidth(0.66f) // 2/3 of the width
            ) {
                Text(text = "Exit")
            }
        }
    }
}

//Preview functions below

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    DisplayMenu(
        modifier = Modifier.fillMaxSize(),
        onScanCardsClicked = { /* TODO: Preview action */ },
        onViewScannedCardsClicked = { /* TODO: Preview action */ },
        onExitClicked = { /* TODO: Preview action */ }
    )
}