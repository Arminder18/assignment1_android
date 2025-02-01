package com.example.assignment1_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserInputScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInputScreen() {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Initialize the fields with default values
    var id by remember { mutableStateOf("158") }
    var username by remember { mutableStateOf("") }
    var courseName by remember { mutableStateOf("") }

    // Variables for displaying the stored data
    var displayedId by remember { mutableStateOf("158") }
    var displayedUsername by remember { mutableStateOf("") }
    var displayedCourseName by remember { mutableStateOf("") }

    // Collect data from DataStore
    val storedId by dataStoreManager.getId.collectAsState(initial = "158") // Default to 158
    val storedUsername by dataStoreManager.getUserName.collectAsState(initial = "")
    val storedCourseName by dataStoreManager.getCourseName.collectAsState(initial = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5EE))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header Section
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Student Details",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Input Fields
        TextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth().height(70.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().height(70.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )
        TextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = { Text("Course Name") },
            modifier = Modifier.fillMaxWidth().height(70.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Store Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        // Store data in DataStore and update displayed variables
                        dataStoreManager.storeData(id, username, courseName)
                        displayedId = id
                        displayedUsername = username
                        displayedCourseName = courseName
                    }
                },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Store", fontSize = 20.sp)
            }

            // Load Button
            Button(
                onClick = {
                    displayedId = storedId
                    displayedUsername = storedUsername
                    displayedCourseName = storedCourseName
                },
                modifier = Modifier.weight(1f).height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Load", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reset Button
        Button(
            onClick = {
                coroutineScope.launch {
                    dataStoreManager.resetData()
                    // Reset local state to default
                    id = "158"
                    username = ""
                    courseName = ""
                    // Reset displayed values as well
                    displayedId = "158"
                    displayedUsername = ""
                    displayedCourseName = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Reset", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display Student Details at Bottom Left
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = "Student Name: $displayedUsername \nStudent ID: $displayedId\nCourse Name: $displayedCourseName",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
