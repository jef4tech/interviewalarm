package com.jef4tech.interviewalarm.presentation.jobs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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

val TertiaryFixed = Color(0xFFFFDEA9)
val PrimaryFixed = Color(0xFFD1E4FF)
val SecondaryContainer = Color(0xFFAEeecb)
val OnSecondaryFixedVariant = Color(0xFF0E5138)
val SurfaceContainerHighest = Color(0xFFE1E2E8)
val OnSurfaceVariant = Color(0xFF414750)
val SurfaceContainerHigh = Color(0xFFE6E8EE)
val PrimaryContainer = Color(0xFF0061A4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: JobViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToAddEdit: (String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val jobs = uiState.upcomingInterviews

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
                            imageVector = Icons.Default.Notifications,
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
                onClick = { onNavigateToAddEdit(null) },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Interview")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 100.dp)
        ) {
            item {
                Text(
                    text = "UPCOMING SESSIONS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryColor,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your Schedule",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDarkBlue
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                CalendarToggle()
                Spacer(modifier = Modifier.height(24.dp))
                WeekStrip()
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (jobs.isEmpty()) {
                item {
                    Text(text = "No upcoming interviews scheduled.", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(jobs) { job ->
                    AgendaItem(
                        job = job,
                        onClick = { onNavigateToAddEdit(job.id) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun CalendarToggle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLow, RoundedCornerShape(50))
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(PrimaryContainer, RoundedCornerShape(50))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Week", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Month", color = OnSurfaceVariant, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun WeekStrip() {
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    val dates = listOf("12", "13", "14", "15", "16")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(days.size) { index ->
            val isSelected = index == 0
            Column(
                modifier = Modifier
                    .size(width = 64.dp, height = 96.dp)
                    .background(
                        if (isSelected) SurfaceContainerLowest else SurfaceContainerLow,
                        RoundedCornerShape(12.dp)
                    )
                    .let {
                        if (isSelected) {
                            it.background(SurfaceContainerLowest, RoundedCornerShape(12.dp))
                              // Simplified representation of the left border
                        } else it
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isSelected) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(PrimaryBlue, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)).align(Alignment.CenterStart))
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = days[index].uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                            Text(text = dates[index], fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                    }
                } else {
                    Text(text = days[index].uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                    Text(text = dates[index], fontSize = 20.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                }

            }
        }
    }
}

@Composable
fun AgendaItem(job: Job, onClick: () -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("h:mm")
    val amPmFormatter = DateTimeFormatter.ofPattern("a")
    
    val timeStr = job.interviewDateTime?.format(dateFormatter) ?: "TBD"
    val amPmStr = job.interviewDateTime?.format(amPmFormatter)?.uppercase() ?: ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLowest)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(modifier = Modifier.width(6.dp).fillMaxHeight().background(PrimaryBlue))
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Box(modifier = Modifier.background(TertiaryFixed, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                            Text(text = "UPCOMING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TertiaryColor, letterSpacing = 0.5.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = job.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(text = "${job.company} • ${job.type.name}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = OnSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.background(PrimaryFixed, RoundedCornerShape(12.dp)).padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = timeStr, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryDarkBlue)
                        Text(text = amPmStr, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PrimaryDarkBlue)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = SurfaceContainerLow)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.background(SecondaryContainer, RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = if (job.isOnline) Icons.Default.PlayArrow else Icons.Default.LocationOn
                        val locationText = if (job.isOnline) "Online" else (if (!job.location.isNullOrEmpty()) job.location else "Location TBD")
                        Icon(imageVector = icon, contentDescription = null, tint = OnSecondaryFixedVariant, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = locationText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnSecondaryFixedVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    if (job.isOnline && !job.location.isNullOrEmpty()) {
                        Text(
                            text = "Join Link",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MockInterviewPromo() {
    val gradient = Brush.linearGradient(
        colors = listOf(PrimaryDarkBlue.copy(alpha = 0.9f), PrimaryContainer.copy(alpha = 0.8f))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Placeholder for image
        Box(modifier = Modifier.fillMaxSize().background(PrimaryContainer))
        Box(modifier = Modifier.fillMaxSize().background(gradient))
        
        Column(
            modifier = Modifier.padding(32.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Need a Mock Interview?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Practice with AI to boost your confidence before the big day.", fontSize = 14.sp, color = PrimaryFixed, modifier = Modifier.width(200.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = PrimaryBlue),
                shape = RoundedCornerShape(50)
            ) {
                Text("START PREP", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
            }
        }
    }
}

