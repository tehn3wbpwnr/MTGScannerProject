package com.example.mtgscannerproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.mtgscannerproject.ui.theme.MTGScannerProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MTGScannerProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DisplayMenu(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayMenu(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        DisplayTitle(text = "Menu")
        DisplayButtons()
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
fun DisplayButtons(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between title and buttons

        Button(onClick = { /* TODO: Add scan card action */ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Scan Cards")
        }

        Spacer(modifier = Modifier.height(8.dp)) // Space between buttons

        Button(
            onClick = { /* TODO: Add view scanned cards action */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "View Scanned Cards")
        }

        Spacer(modifier = Modifier.height(8.dp)) // Space between buttons

        Button(onClick = { /* TODO: Add exit action */ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Exit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DisplayMenu(
        modifier = Modifier
            .fillMaxSize()
    )
}