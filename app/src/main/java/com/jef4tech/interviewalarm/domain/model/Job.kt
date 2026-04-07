package com.jef4tech.interviewalarm.domain.model

import java.time.LocalDateTime

enum class JobType {
    REMOTE, OFFICE, HYBRID
}

enum class JobStatus {
    APPLIED, INTERVIEWING, REJECTED, OFFERED
}

data class Job(
    val id: String,
    val title: String,
    val company: String,
    val type: JobType,
    val salaryRange: String?,
    val location: String?,
    val interviewDateTime: LocalDateTime?,
    val status: JobStatus,
    val isOnline: Boolean = true,
    val notes: String = "",
    val reminders: String = ""
)
