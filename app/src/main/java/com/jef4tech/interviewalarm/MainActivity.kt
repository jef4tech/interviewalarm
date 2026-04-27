package com.jef4tech.interviewalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jef4tech.interviewalarm.presentation.JobViewModel
import com.jef4tech.interviewalarm.presentation.add_edit_job.AddEditJobScreen
import com.jef4tech.interviewalarm.presentation.job_detail.InterviewDetailScreen
import com.jef4tech.interviewalarm.presentation.jobs.InterviewDashboard
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        bottomBar = {
                            NavigationBar(
                                containerColor = Color(0xFFF7FAF9),
                                contentColor = Color(0xFF191C1C)
                            ) {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                    label = { Text("Home") },
                                    selected = currentRoute == "job_list" || currentRoute?.startsWith("job_detail") == true,
                                    onClick = { 
                                        if (currentRoute == "job_list" || currentRoute?.startsWith("job_detail") == true) {
                                            navController.popBackStack("job_list", inclusive = false)
                                        } else {
                                            navController.navigate("job_list") {
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF00464A),
                                        selectedTextColor = Color(0xFF00464A),
                                        indicatorColor = Color(0xFFCFE6F2)
                                    )
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Schedule") },
                                    label = { Text("Schedule") },
                                    selected = currentRoute == "schedule",
                                    onClick = {
                                        navController.navigate("schedule") {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF00464A),
                                        selectedTextColor = Color(0xFF00464A),
                                        indicatorColor = Color(0xFFCFE6F2)
                                    )
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                                    label = { Text("Settings") },
                                    selected = currentRoute == "settings",
                                    onClick = { 
                                        navController.navigate("settings") {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF00464A),
                                        selectedTextColor = Color(0xFF00464A),
                                        indicatorColor = Color(0xFFCFE6F2)
                                    )
                                )
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController, 
                            startDestination = "job_list",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable("job_list") {
                                val viewModel: JobViewModel = hiltViewModel()
                                InterviewDashboard(
                                    viewModel = viewModel,
                                    onNavigateToDetail = { jobId ->
                                        navController.navigate("job_detail/$jobId")
                                    },
                                    onNavigateToAddEdit = {
                                        navController.navigate("add_edit_job")
                                    }
                                )
                            }

                            composable(
                                route = "job_detail/{jobId}",
                                arguments = listOf(navArgument("jobId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
                                InterviewDetailScreen(
                                    jobId = jobId,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable(
                                route = "add_edit_job?jobId={jobId}",
                                arguments = listOf(navArgument("jobId") { 
                                    type = NavType.StringType
                                    nullable = true 
                                    defaultValue = null
                                })
                            ) { backStackEntry ->
                                val jobId = backStackEntry.arguments?.getString("jobId")
                                val viewModel: JobViewModel = hiltViewModel()
                                val uiState by viewModel.uiState.collectAsState()
                                val jobToEdit = uiState.upcomingInterviews.find { it.id == jobId }

                                AddEditJobScreen(
                                    jobId = jobId,
                                    jobToEdit = jobToEdit,
                                    onSaveJob = { title, comp, date, type, salary, loc, inline, nts, rem -> 
                                        viewModel.saveJob(
                                            id = jobId,
                                            title = title,
                                            company = comp,
                                            interviewDateTime = date,
                                            type = type,
                                            salaryRange = salary,
                                            location = loc,
                                            isOnline = inline,
                                            notes = nts,
                                            reminders = rem
                                        ) 
                                    },
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }

                            composable("settings") {
                                com.jef4tech.interviewalarm.presentation.settings.SettingsScreen(
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                            composable("schedule") {
                                val viewModel: JobViewModel = hiltViewModel()
                                com.jef4tech.interviewalarm.presentation.jobs.ScheduleScreen(
                                    viewModel = viewModel,
                                    onNavigateToHome = {
                                        navController.navigate("job_list") {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    onNavigateToAddEdit = { jobId -> 
                                        if (jobId != null) {
                                            navController.navigate("add_edit_job?jobId=$jobId")
                                        } else {
                                            navController.navigate("add_edit_job")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
