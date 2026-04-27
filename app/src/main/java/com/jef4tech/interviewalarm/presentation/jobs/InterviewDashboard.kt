package com.jef4tech.interviewalarm.presentation.jobs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.model.JobStatus
import com.jef4tech.interviewalarm.domain.model.JobType
import com.jef4tech.interviewalarm.presentation.JobViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val SurfaceColor = Color(0xFFF8F9FF)
val PrimaryBlue = Color(0xFF0061A4)
val PrimaryDarkBlue = Color(0xFF00497D)
val OnSurface = Color(0xFF191C20)
val SurfaceContainerLow = Color(0xFFF2F3FA)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val OutlineColor = Color(0xFF717782)
val SecondaryColor = Color(0xFF2C694E)
val TertiaryColor = Color(0xFF5F4200)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterviewDashboard(
    viewModel: JobViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAddEdit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = SurfaceColor,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Precision Prep",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Notifications",
                            tint = OutlineColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddEdit,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Interview")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            val jobs = uiState.upcomingInterviews
            val nextInterview = jobs.firstOrNull { it.status == JobStatus.INTERVIEWING }
                ?: jobs.firstOrNull()

            val appliedCount = uiState.appliedCount
            val interviewsCount = uiState.interviewsCount
            val offersCount = uiState.offersCount

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 100.dp)
            ) {
                if (nextInterview != null) {
                    item {
                        HeroSection(job = nextInterview, onClick = { onNavigateToDetail(nextInterview.id) })
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                item {
                    StatsSection(applied = appliedCount, interviews = interviewsCount, offers = offersCount)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Upcoming Prep",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = "View Calendar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue,
                            modifier = Modifier.clickable { }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (jobs.isEmpty()) {
                    item {
                        Text(
                            text = "No upcoming interviews.",
                            modifier = Modifier.padding(16.dp),
                            color = OutlineColor
                        )
                    }
                } else {
                    items(jobs) { job ->
                        JobItem(job = job, modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable { onNavigateToDetail(job.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun HeroSection(job: Job, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        colors = listOf(PrimaryDarkBlue, PrimaryBlue)
    )
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "COMING UP NEXT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = job.company.ifBlank { "Company" },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = job.title,
                        fontSize = 16.sp,
                        color = Color(0xFFD1E4FF),
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.15f), shape = CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = job.interviewDateTime?.format(timeFormatter) ?: "TBD",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Join Meeting", // Default text since we don't have exact time calculation here easily
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                Button(
                    onClick = { /* Join */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Join Meeting", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = onClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = null, // Custom glass panel approach
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .height(60.dp)
                ) {
                    Text("Details", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatsSection(applied: Int, interviews: Int, offers: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(modifier = Modifier.weight(1f), title = "APPLIED", value = applied.toString(), color = PrimaryBlue, progress = 0.6f)
        StatCard(modifier = Modifier.weight(1f), title = "INTERVIEWS", value = interviews.toString(), color = TertiaryColor, progress = 0.5f)
        StatCard(modifier = Modifier.weight(1f), title = "OFFERS", value = offers.toString(), color = SecondaryColor, progress = 0.25f)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, color: Color, progress: Float) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = OutlineColor,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(32.dp)
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(progress)
                        .background(color, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

@Composable
fun JobItem(job: Job, modifier: Modifier = Modifier) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd • hh:mm a")

    val statusColor = when (job.status) {
        JobStatus.INTERVIEWING -> Color(0xFF0061A4)
        JobStatus.APPLIED -> Color(0xFF2C694E)
        JobStatus.OFFERED -> SecondaryColor
        JobStatus.REJECTED -> Color(0xFFBA1A1A)
    }
    val statusBg = statusColor.copy(alpha = 0.1f)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(SurfaceContainerLow, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = job.company.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = OutlineColor,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = job.company,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = job.title,
                        fontSize = 14.sp,
                        color = OutlineColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = job.status.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = job.interviewDateTime?.format(dateFormatter) ?: "Unscheduled",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OutlineColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JobItemPreview() {
    MaterialTheme {
        JobItem(
            job = Job(
                id = "1",
                title = "Senior Designer",
                company = "Google",
                type = JobType.REMOTE,
                salaryRange = null,
                location = null,
                interviewDateTime = LocalDateTime.now().plusHours(2),
                status = JobStatus.INTERVIEWING
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

