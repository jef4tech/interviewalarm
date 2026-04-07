package com.jef4tech.interviewalarm.domain.usecase

import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.repository.JobRepository
import javax.inject.Inject

class GetJobByIdUseCase @Inject constructor(
    private val repository: JobRepository
) {
    suspend operator fun invoke(id: String): Job? {
        return repository.getJobById(id)
    }
}
