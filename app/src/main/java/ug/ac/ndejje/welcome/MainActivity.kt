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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
fun StudentInfo(student: Student) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = student.profileImageId),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = student.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = student.regNumber, color = Color.Gray)
        if (student.isVerified) {
            Text("Verified Student", color = Color(0xFF4CAF50))
        }
    }
}
@Composable
fun StudentIdCard(student: Student) {

    // STEP B1: Each card gets its own private state, starting as false
    var isPresent by remember { mutableStateOf(false) }

    // STEP B2: Derive border colour from the current state
    val borderColor = if (isPresent) Color.Green else Color.Transparent

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp)), // STEP B3
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StudentInfo(student)

            // STEP B4: Button label and colour both react to state
            Button(
                onClick = { isPresent = !isPresent }, // Toggle on every click
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPresent) Color.Gray
                    else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isPresent) "Present" else "Mark Present")
            }
        }
    }
}
@Composable
fun StudentDirectory() {

    // STEP A1: Declare state to hold whatever the user types
    var searchQuery by remember { mutableStateOf("") }

    // STEP A2: Derive a filtered list — recalculates every time searchQuery changes
    val filteredStudents = StudentProvider.studentList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    // STEP A3: Column places the TextField above the LazyColumn
    Column(modifier = Modifier.fillMaxSize()) {

        // STEP A4: The search input field
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it }, // Updates state on every keystroke
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            }
        )

        // STEP A5: The list now uses filteredStudents, NOT the full list
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(filteredStudents) { student ->
                StudentIdCard(student = student)
                Spacer(modifier = Modifier.height(12.dp))
            }
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