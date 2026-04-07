package com.jef4tech.interviewalarm.domain.usecase

import com.jef4tech.interviewalarm.domain.model.Job
import com.jef4tech.interviewalarm.domain.model.JobStatus
import com.jef4tech.interviewalarm.domain.model.JobType
import com.jef4tech.interviewalarm.domain.repository.JobRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class SaveJobUseCase @Inject constructor(
    private val repository: JobRepository,
    private val scheduleInterviewAlarmUseCase: ScheduleInterviewAlarmUseCase
) {
    suspend operator fun invoke(
        id: String?, 
        title: String, 
        company: String, 
        interviewDateTime: LocalDateTime?,
        type: JobType = JobType.OFFICE,
        salaryRange: String? = null,
        location: String? = null,
        status: JobStatus = JobStatus.INTERVIEWING,
        isOnline: Boolean = true,
        notes: String = "",
        reminders: String = ""
    ) {
        val job = Job(
            id = id ?: UUID.randomUUID().toString(),
            title = title,
            company = company,
            type = type,
            salaryRange = salaryRange,
            location = location,
            interviewDateTime = interviewDateTime,
            status = status,
            isOnline = isOnline,
            notes = notes,
            reminders = reminders
        )
        
        // Actually insert into Room!
        if (id == null) {
            repository.insertJob(job)
        } else {
            repository.updateJob(job)
        }
        
        // Schedule exact alarms physically locally
        scheduleInterviewAlarmUseCase(job)
    }
}
