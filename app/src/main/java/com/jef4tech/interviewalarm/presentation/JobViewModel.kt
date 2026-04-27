package com.jef4tech.interviewalarm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jef4tech.interviewalarm.domain.model.Job

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.jef4tech.interviewalarm.domain.usecase.SaveJobUseCase
import com.jef4tech.interviewalarm.domain.usecase.GetAllJobsUseCase
import com.jef4tech.interviewalarm.domain.model.JobStatus

data class JobUiState(
    val isLoading: Boolean = false,
    val upcomingInterviews: List<Job> = emptyList(),
    val appliedCount: Int = 0,
    val interviewsCount: Int = 0,
    val offersCount: Int = 0,
    val error: String? = null
)

@HiltViewModel
class JobViewModel @Inject constructor(
    private val getAllJobsUseCase: GetAllJobsUseCase,
    private val saveJobUseCase: SaveJobUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobUiState())
    val uiState: StateFlow<JobUiState> = _uiState.asStateFlow()

    init {
        fetchAllJobs()
    }

    private fun fetchAllJobs() {
        viewModelScope.launch {
            getAllJobsUseCase()
                .onStart {
                    android.util.Log.d("InterviewDebugUI", "fetchAllJobs: Started")
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    android.util.Log.e("InterviewDebugUI", "fetchAllJobs error:", e)
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { jobs ->
                    val upcoming = jobs.filter { it.status == JobStatus.INTERVIEWING }
                    val applied = jobs.count { it.status == JobStatus.APPLIED }
                    val interviews = upcoming.size
                    val offers = jobs.count { it.status == JobStatus.OFFERED }
                    
                    android.util.Log.d("InterviewDebugUI", "fetchAllJobs: Collected ${jobs.size} jobs total, ${upcoming.size} upcoming")
                    
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            upcomingInterviews = upcoming,
                            appliedCount = applied,
                            interviewsCount = interviews,
                            offersCount = offers
                        ) 
                    }
                }
        }
    }

    fun saveJob(
        id: String? = null,
        title: String,
        company: String,
        interviewDateTime: java.time.LocalDateTime?,
        type: com.jef4tech.interviewalarm.domain.model.JobType = com.jef4tech.interviewalarm.domain.model.JobType.OFFICE,
        salaryRange: String? = null,
        location: String? = null,
        isOnline: Boolean = true,
        notes: String = "",
        reminders: String = ""
    ) {
        android.util.Log.d("InterviewDebugUI", "saveJob: UI requested save -> title: $title, date: $interviewDateTime")
        viewModelScope.launch {
            saveJobUseCase(
                id = id,
                title = title,
                company = company,
                interviewDateTime = interviewDateTime,
                type = type,
                salaryRange = salaryRange,
                location = location,
                isOnline = isOnline,
                notes = notes,
                reminders = reminders
            )
        }
    }
}
