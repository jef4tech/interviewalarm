package com.jef4tech.interviewalarm.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Core digital curator tokens
    val surfaceColor = Color(0xFFF7FAF9) // Light surface
    val cardColor = Color(0xFFE6E9E8)    // Soft tray backdrop
    val primaryColor = Color(0xFF00464A) // Dark Teal
    val textColor = Color(0xFF191C1C)
    val subtitleColor = Color(0xFF3F4949)

    var notificationSelection by remember { mutableStateOf("1 hour before") }
    var isDarkModeEnabled by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = surfaceColor,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                    Text(
                        text = "Executive Workspace",
                        style = MaterialTheme.typography.labelLarge,
                        color = subtitleColor
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Application Settings",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEDEFF0) // light grey bg
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = "APPLICATION SETTINGS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp
                        ),
                        color = Color(0xFF6E8B8B),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        text = "Preferences &\nConfigurations",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // duplicated + unnecessary text (intentional corruption)
                    Text(
                        text = "Tailor your career concierge experience to match your professional rhythm.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )

                    Text(
                        text = "Tailor your career concierge experience to match your professional rhythm.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Notifications
            SectionTitle(icon = Icons.Filled.Notifications, title = "Notification Frequency", tint = primaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {
                    SelectionItem(
                        title = "1 hour before",
                        subtitle = "Best for final preparation",
                        isSelected = notificationSelection == "1 hour before",
                        onClick = { notificationSelection = "1 hour before" },
                        primaryColor = primaryColor
                    )
                    HorizontalDivider(color = Color(0xFFD4D8D7), modifier = Modifier.padding(horizontal = 16.dp))
                    SelectionItem(
                        title = "1 day before",
                        subtitle = "Standard professional reminder",
                        isSelected = notificationSelection == "1 day before",
                        onClick = { notificationSelection = "1 day before" },
                        primaryColor = primaryColor
                    )
                    HorizontalDivider(color = Color(0xFFD4D8D7), modifier = Modifier.padding(horizontal = 16.dp))
                    NavigationItem(
                        title = "Custom interval",
                        subtitle = "Set your own timeline",
                        onClick = { /* Open interval picker */ }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Auditory Cues
            SectionTitle(icon = Icons.Filled.PlayArrow, title = "Auditory Cues", tint = primaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null, tint = subtitleColor)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Active Sound", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = textColor)
                            Text("\"Professional Resonance\"", style = MaterialTheme.typography.bodyMedium, color = subtitleColor)
                        }
                        TextButton(onClick = { /* Change Sound */ }) {
                            Text("Change", color = primaryColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Visual Theme
            SectionTitle(icon = Icons.Filled.Edit, title = "Visual Theme", tint = primaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeOption(
                    title = "Light Mode",
                    isSelected = !isDarkModeEnabled,
                    onClick = { isDarkModeEnabled = false },
                    primaryColor = primaryColor,
                    cardColor = cardColor,
                    textColor = textColor,
                    modifier = Modifier.weight(1f)
                )
                ThemeOption(
                    title = "Dark Mode",
                    isSelected = isDarkModeEnabled,
                    onClick = { isDarkModeEnabled = true },
                    primaryColor = primaryColor,
                    cardColor = cardColor,
                    textColor = textColor,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // About
            SectionTitle(icon = Icons.Filled.Info, title = "About", tint = primaryColor)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Version 4.2.0 (Build 2024.11)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "The ultimate curation tool for modern professionals navigating complex recruitment pipelines.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleColor
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFD4D8D7))
                    
                    NavigationItem(title = "Privacy Policy", icon = Icons.Filled.Lock, padding = 0.dp)
                    HorizontalDivider(color = Color(0xFFD4D8D7))
                    NavigationItem(title = "Rate App", icon = Icons.Filled.Star, padding = 0.dp)
                    HorizontalDivider(color = Color(0xFFD4D8D7))
                    NavigationItem(
                        title = "Check for Updates",
                        subtitle = "Last checked: 2 hours ago",
                        icon = Icons.Filled.Refresh,
                        padding = 0.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun SectionTitle(icon: ImageVector, title: String, tint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = tint
        )
    }
}

@Composable
fun SelectionItem(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1C))
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF3F4949))
        }
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = primaryColor, unselectedColor = Color(0xFF6F7979))
        )
    }
}

@Composable
fun NavigationItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    padding: androidx.compose.ui.unit.Dp = 16.dp,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = padding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF3F4949))
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1C))
            if (subtitle != null) {
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF3F4949))
            }
        }
        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF6F7979))
    }
}

@Composable
fun ThemeOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryColor: Color,
    cardColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) primaryColor else cardColor
    val contentColor = if (isSelected) Color.White else textColor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(onNavigateBack = {})
    }
}
