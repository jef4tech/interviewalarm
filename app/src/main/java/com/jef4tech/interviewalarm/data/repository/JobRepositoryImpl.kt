package com.jef4tech.interviewalarm.data.repository

import android.util.Log
import com.jef4tech.interviewalarm.data.local.JobDao
import com.jef4tech.interviewalarm.data.local.JobEntity
import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val dao: JobDao
) : JobRepository {

    override fun getAllJobs(): Flow<List<Job>> {
        return dao.getAllJobs().map { entities ->
            entities.map { it.toDomain() }
        }
    }



    override suspend fun getJobById(id: String): Job? {
        return dao.getJobById(id)?.toDomain()
    }

    override suspend fun insertJob(job: Job) {
        dao.insertJob(job.toEntity())
    }

    override suspend fun updateJob(job: Job) {
        dao.updateJob(job.toEntity())
    }

    override suspend fun deleteJob(job: Job) {
        dao.deleteJob(job.toEntity())
    }
}

// Mapper extension functions
fun JobEntity.toDomain(): Job {
    return Job(id, title, company, type, salaryRange, location, interviewDateTime, status, isOnline, notes, reminders)
}

fun Job.toEntity(): JobEntity {
    return JobEntity(id, title, company, type, salaryRange, location, interviewDateTime, status, isOnline, notes, reminders)
}
