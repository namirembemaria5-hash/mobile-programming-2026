package ug.ac.ndejje.welcome


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ug.ac.ndejje.welcome.ui.theme.NdejjeWelcomeAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NdejjeWelcomeAppTheme {

                StudentDirectory()
            }
        }
    }

}


@Composable
fun StudentInfo(student: Student, isDetailView: Boolean = false, onBack: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Show Back Button ONLY in Detail View
        if (isDetailView) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Image(
            painter = painterResource(id = student.profileImageId),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(if (isDetailView) 200.dp else 120.dp) // Make image bigger in detail view
                .clip(RoundedCornerShape(8.dp))
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = student.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = student.regNumber, color = Color.Gray)

        if (student.isVerified) {
            Text("Verified Student", color = Color(0xFF4CAF50))
        }

        // Add extra details if in Detail View
        if (isDetailView) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to the student profile. Here you can find more specific academic records and department details.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StudentIdCard(student: Student, onViewProfile: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Reusing StudentInfo but without the back button for the list card
            StudentInfo(student = student, isDetailView = false)

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onViewProfile) {
                Text("View Profile")
            }
        }
    }
}

@Composable
fun StudentDirectory() {
    // State for searching
    var searchQuery by remember { mutableStateOf("") }

    // State for navigation: holds the student to display, or null if showing the list
    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    // Logic to switch screens
    if (selectedStudent == null) {
        // --- LIST VIEW ---
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search students...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                }
            )

            val filteredStudents = StudentProvider.studentList.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                items(filteredStudents) { student ->
                    StudentIdCard(
                        student = student,
                        onViewProfile = { selectedStudent = student }
                    )
                }
            }
        }
    } else {
        // --- DETAIL VIEW ---
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            StudentInfo(
                student = selectedStudent!!,
                isDetailView = true,
                onBack = { selectedStudent = null }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    NdejjeWelcomeAppTheme {
        StudentDirectory()
    }
}