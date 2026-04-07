package com.jef4tech.interviewalarm.presentation.job_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.usecase.GetJobByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobDetailUiState(
    val isLoading: Boolean = false,
    val job: Job? = null,
    val error: String? = null
)

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val getJobByIdUseCase: GetJobByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobDetailUiState())
    val uiState: StateFlow<JobDetailUiState> = _uiState.asStateFlow()

    fun loadJob(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val fetchedJob = getJobByIdUseCase(id)
                _uiState.update { it.copy(isLoading = false, job = fetchedJob) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
