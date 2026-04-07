package com.jef4tech.interviewalarm.presentation.add_edit_job

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

import androidx.compose.ui.tooling.preview.Preview
import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.model.JobType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditJobScreen(
    jobId: String? = null,
    jobToEdit: Job? = null,
    onSaveJob: (String, String, LocalDateTime?, JobType, String?, String?, Boolean, String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(JobType.OFFICE) }
    var isOnline by remember { mutableStateOf(true) }
    
    // Salary Range state
    var sliderPosition by remember { mutableStateOf(50f..150f) }
    var minSalary by remember { mutableStateOf("50") }
    var maxSalary by remember { mutableStateOf("150") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    
    // Core design system colors from provided design
    val primaryColor = Color(0xFF0061A4)
    val primaryContainer = Color(0xFFD1E4FF)
    val onPrimaryContainer = Color(0xFF001D36)
    val surfaceColor = Color(0xFFF8F9FF)
    val surfaceContainerLowest = Color(0xFFFFFFFF)
    val surfaceContainerLow = Color(0xFFF2F3FA)
    val surfaceContainerHigh = Color(0xFFE6E8EE)
    val onSurfaceVariant = Color(0xFF414750)
    val outlineVariant = Color(0xFFC1C7D2)
    val tertiaryColor = Color(0xFFFFDEA9)
    val onTertiaryColor = Color(0xFF5E4100)
    
    var notes by remember { mutableStateOf("") }
    var selectedReminders by remember { mutableStateOf(setOf("10 min", "1 hr")) }
    val reminderOptions = listOf("10 min", "30 min", "1 hr", "1 day", "Custom")
    
    LaunchedEffect(jobToEdit) {
        jobToEdit?.let { job ->
            title = job.title
            company = job.company
            location = job.location ?: ""
            selectedType = job.type
            isOnline = job.isOnline
            notes = job.notes ?: ""
            if (job.salaryRange != null) {
                val regex = Regex("(\\d+)\\s*LPA\\s*-\\s*(\\d+)\\s*LPA")
                val match = regex.find(job.salaryRange)
                if (match != null) {
                    minSalary = match.groupValues[1]
                    maxSalary = match.groupValues[2]
                    sliderPosition = minSalary.toFloat()..maxSalary.toFloat()
                }
            }
            if (job.interviewDateTime != null) {
                selectedDate = job.interviewDateTime.toLocalDate()
                selectedTime = job.interviewDateTime.toLocalTime()
            }
            if (job.reminders.isNotEmpty()) {
                selectedReminders = job.reminders.split(",").map { it.trim() }.toSet()
            }
        }
    }
    
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                return !date.isBefore(LocalDate.now())
            }
        }
    )
    val timePickerState = rememberTimePickerState()

    val textFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = surfaceContainerLowest,
        focusedContainerColor = surfaceContainerLowest,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        cursorColor = primaryColor,
        unfocusedPlaceholderColor = outlineVariant,
        focusedPlaceholderColor = outlineVariant
    )

    Scaffold(
        containerColor = surfaceColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (jobId == null) "Add Interview" else "Edit Interview",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = primaryColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val finalDateTime = if (selectedDate != null && selectedTime != null) {
                            LocalDateTime.of(selectedDate, selectedTime)
                        } else null
                        
                        onSaveJob(
                            title, 
                            company, 
                            finalDateTime,
                            selectedType,
                            "${minSalary}LPA - ${maxSalary}LPA",
                            location,
                            isOnline,
                            notes,
                            selectedReminders.joinToString(",")
                        )
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = title.isNotBlank() && company.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        disabledContainerColor = Color(0xFFB0CBE1)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "Save Interview",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // Section 1: Basic Info
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text(
                        text = "COMPANY NAME",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )
                    TextField(
                        value = company,
                        onValueChange = { company = it },
                        placeholder = { Text("e.g. Google") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }

                Column {
                    Text(
                        text = "ROLE / JOB TITLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. Senior UX Designer") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }

            // Section 1.5: Expected CTC Range
            Column {
                Text(
                    text = "EXPECTED CTC RANGE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Select your expected salary range",
                    fontSize = 12.sp,
                    color = onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                RangeSlider(
                    value = sliderPosition,
                    onValueChange = { range ->
                        sliderPosition = range
                        minSalary = range.start.roundToInt().toString()
                        maxSalary = range.endInclusive.roundToInt().toString()
                    },
                    valueRange = 0f..500f,
                    colors = SliderDefaults.colors(
                        thumbColor = primaryColor,
                        activeTrackColor = primaryColor,
                        inactiveTrackColor = surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(
                        color = primaryContainer.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "₹${minSalary} LPA — ₹${maxSalary} LPA",
                            color = primaryColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Section 2: Job Type and Date & Time
            Column {
                Text(
                    text = "JOB TYPE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    JobType.values().forEach { type ->
                        val isSelected = type == selectedType
                        Surface(
                            color = if(isSelected) primaryContainer else surfaceContainerLow,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.clickable { selectedType = type }
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(Icons.Filled.Add, "Check", modifier = Modifier.size(16.dp), tint = onPrimaryContainer)
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(
                                    type.name.lowercase().capitalize(),
                                    fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = if(isSelected) onPrimaryContainer else onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Date & Time - Asymmetric Bento Pattern
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Date Bento
                Surface(
                    color = surfaceContainerLow,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showDatePicker = true }
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Using text placeholder since some icons might need imports, but we use what we have
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Date", tint = primaryColor, modifier = Modifier.size(20.dp))
                            Text(text = "DATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = primaryColor)
                        }
                        Text(
                            text = selectedDate?.format(DateTimeFormatter.ofPattern("MMMM dd")) ?: "Select Date",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF191C1C)
                        )
                    }
                }
                
                // Time Bento
                Surface(
                    color = surfaceContainerLow,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showTimePicker = true }
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Time", tint = primaryColor, modifier = Modifier.size(20.dp))
                            Text(text = "TIME", fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = primaryColor)
                        }
                        Text(
                            text = selectedTime?.format(DateTimeFormatter.ofPattern("h:mm a")) ?: "Select Time",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF191C1C)
                        )
                    }
                }
            }

            // Section 3: Interview Type & Online Link
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(
                    color = surfaceContainerHigh,
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val types = listOf(true to "Online", false to "Offline")
                        types.forEach { (online, label) ->
                            val isSelected = online == isOnline
                            val bgColor = if (isSelected) primaryContainer else Color.Transparent
                            val textColor = if (isSelected) onPrimaryContainer else onSurfaceVariant
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(bgColor, RoundedCornerShape(32.dp))
                                    .clickable { isOnline = online }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = textColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                if (isOnline) {
                    Column {
                        Text(
                            text = "ONLINE LINK",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = onSurfaceVariant,
                            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                        )
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            placeholder = { Text("https://meet.google.com/...") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors,
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Filled.Add, "Link", tint = outlineVariant)
                            }
                        )
                    }
                } else {
                    Column {
                        Text(
                            text = "LOCATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = onSurfaceVariant,
                            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                        )
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            placeholder = { Text("e.g. 1600 Amphitheatre Parkway") },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = textFieldColors,
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Filled.Add, "Location", tint = outlineVariant)
                            }
                        )
                    }
                }
            }

            // Section 4: Reminders
            Column {
                Text(
                    text = "REMINDERS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                )
                
                // Wrap equivalent in standard compose Row setup: using Accompanist or just simple Rows
                // For simplicity, using a Row with scrolling or simple rows.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    reminderOptions.take(3).forEach { option ->
                        val isSelected = selectedReminders.contains(option)
                        Surface(
                            color = if(isSelected) primaryContainer else surfaceContainerLow,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.clickable { 
                                if(isSelected) selectedReminders -= option else selectedReminders += option 
                            }
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(Icons.Filled.Add, "Check", modifier = Modifier.size(16.dp), tint = onPrimaryContainer)
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(option, fontWeight = if(isSelected) FontWeight.SemiBold else FontWeight.Medium, 
                                     color = if(isSelected) onPrimaryContainer else onSurfaceVariant, fontSize = 14.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    reminderOptions.drop(3).forEach { option ->
                        val isCustom = option == "Custom"
                        val isSelected = selectedReminders.contains(option)
                        
                        val bgColor = if(isCustom) tertiaryColor else if(isSelected) primaryContainer else surfaceContainerLow
                        val contentColor = if(isCustom) onTertiaryColor else if(isSelected) onPrimaryContainer else onSurfaceVariant
                        
                        Surface(
                            color = bgColor,
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.clickable { 
                                if(isSelected) selectedReminders -= option else selectedReminders += option 
                            }
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (isCustom) {
                                    Icon(Icons.Filled.Add, "Add", modifier = Modifier.size(16.dp), tint = contentColor)
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(option, fontWeight = FontWeight.SemiBold, color = contentColor, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Section 5: Notes
            Column(modifier = Modifier.padding(bottom = 60.dp)) {
                Text(
                    text = "NOTES (OPTIONAL)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                )
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Mention key topics, questions to ask, or interviewer names...") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors,
                    maxLines = 4
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                    showTimePicker = true
                }) {
                    Text("OK", color = primaryColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = primaryColor,
                    todayDateBorderColor = primaryColor,
                    todayContentColor = primaryColor
                )
            )
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("OK", color = primaryColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = surfaceContainerLow,
                        selectorColor = primaryColor,
                        timeSelectorSelectedContainerColor = primaryContainer,
                        timeSelectorSelectedContentColor = primaryColor
                    )
                )
            }
        )
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun AddEditJobScreenPreview() {
    MaterialTheme {
        AddEditJobScreen(
            jobId = null,
            onSaveJob = { _, _, _, _, _, _, _, _, _ -> },
            onNavigateBack = {}
        )
    }
}
