package com.jef4tech.interviewalarm.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jef4tech.interviewalarm.domain.model.JobStatus
import com.jef4tech.interviewalarm.domain.model.JobType
import java.time.LocalDateTime

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
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
