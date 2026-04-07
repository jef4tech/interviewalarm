package com.jef4tech.interviewalarm.data.local

import androidx.room.TypeConverter
import com.jef4tech.interviewalarm.domain.model.JobStatus
import com.jef4tech.interviewalarm.domain.model.JobType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            return LocalDateTime.parse(it, formatter)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toJobType(value: String) = enumValueOf<JobType>(value)

    @TypeConverter
    fun fromJobType(value: JobType) = value.name

    @TypeConverter
    fun toJobStatus(value: String) = enumValueOf<JobStatus>(value)

    @TypeConverter
    fun fromJobStatus(value: JobStatus) = value.name
}
