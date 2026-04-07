package com.jef4tech.interviewalarm.presentation.job_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jef4tech.interviewalarm.domain.model.Job
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewDetailScreen(
    jobId: String,
    onNavigateBack: () -> Unit,
    viewModel: JobDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val job = uiState.job

    LaunchedEffect(jobId) {
        viewModel.loadJob(jobId)
    }

    val scrollState = rememberScrollState()
    
    // Core design system colors (The Digital Curator)
    val primaryColor = Color(0xFF00464A)
    val onSurface = Color(0xFF191C1C)
    val surfaceContainerLow = Color(0xFFF2F4F4)
    val softTrayColor = Color(0xFFE6E9E8)
    val surfaceColor = Color(0xFFF7FAF9)

    Scaffold(
        containerColor = surfaceColor,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = onSurface
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (job != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Headline Section
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 36.sp),
                    fontWeight = FontWeight.Bold,
                    color = onSurface,
                    lineHeight = 40.sp,
                    letterSpacing = (-1).sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = job.company,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF3F4949)
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Info Cards (No formal borders, purely tonal changes)
                val cardColors = CardDefaults.cardColors(containerColor = surfaceContainerLow)
                val cardShape = RoundedCornerShape(16.dp)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Schedule Card
                    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
                    val dateStr = job.interviewDateTime?.format(dateFormatter) ?: "TBD"
                    val timeStr = job.interviewDateTime?.format(timeFormatter) ?: "TBD"
                    
                    Card(
                        colors = cardColors,
                        shape = cardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("SCHEDULE", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = primaryColor, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(dateStr, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = onSurface)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(timeStr, fontSize = 13.sp, color = Color(0xFF3F4949))
                        }
                    }

                    // Compensation Card
                    Card(
                        colors = cardColors,
                        shape = cardShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("COMPENSATION", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = primaryColor, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(job.salaryRange ?: "Not specified", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = onSurface)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Annual", fontSize = 13.sp, color = Color(0xFF3F4949))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

            // Reminders Card
            Card(
                colors = cardColors,
                shape = cardShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("REMINDERS", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = primaryColor, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Alerting 1 hour before start", fontSize = 14.sp, color = onSurface)
                    }
                    Switch(
                        checked = true, 
                        onCheckedChange = null,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Preparation Notes
            Text(
                text = "Interview Preparation Notes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // "Glass" container for notes
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = job.notes,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF3F4949),
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp)) // padding for bottom nav
            }
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(6.dp)
                .background(Color(0xFF00464A), CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF191C1C),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InterviewDetailScreenPreview() {
    MaterialTheme {
        InterviewDetailScreen(jobId = "test", onNavigateBack = {})
    }
}
