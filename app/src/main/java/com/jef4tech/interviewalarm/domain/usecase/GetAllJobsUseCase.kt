package com.jef4tech.interviewalarm.domain.usecase

import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllJobsUseCase @Inject constructor(
    private val repository: JobRepository
) {
    operator fun invoke(): Flow<List<Job>> {
        return repository.getAllJobs()
    }
}
