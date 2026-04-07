package com.jef4tech.interviewalarm.domain.repository

import com.jef4tech.interviewalarm.domain.model.Job
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface JobRepository {
    fun getAllJobs(): Flow<List<Job>>
    fun getUpcomingInterviews(): Flow<List<Job>>
    suspend fun getJobById(id: String): Job?
    suspend fun insertJob(job: Job)
    suspend fun updateJob(job: Job)
    suspend fun deleteJob(job: Job)
}
