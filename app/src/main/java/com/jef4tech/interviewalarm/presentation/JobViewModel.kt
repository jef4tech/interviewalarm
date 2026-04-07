package com.jef4tech.interviewalarm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.usecase.GetUpcomingInterviewsUseCase
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

data class JobUiState(
    val isLoading: Boolean = false,
    val upcomingInterviews: List<Job> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class JobViewModel @Inject constructor(
    private val getUpcomingInterviewsUseCase: GetUpcomingInterviewsUseCase,
    private val saveJobUseCase: SaveJobUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobUiState())
    val uiState: StateFlow<JobUiState> = _uiState.asStateFlow()

    init {
        fetchUpcomingInterviews()
    }

    private fun fetchUpcomingInterviews() {
        viewModelScope.launch {
            getUpcomingInterviewsUseCase()
                .onStart {
                    android.util.Log.d("InterviewDebugUI", "fetchUpcomingInterviews: Started")
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { e ->
                    android.util.Log.e("InterviewDebugUI", "fetchUpcomingInterviews error:", e)
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { jobs ->
                    android.util.Log.d("InterviewDebugUI", "fetchUpcomingInterviews: Collected ${jobs.size} jobs to show in UI")
                    _uiState.update { 
                        it.copy(isLoading = false, upcomingInterviews = jobs)
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
