package com.jef4tech.interviewalarm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [JobEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class InterviewAlarmDatabase : RoomDatabase() {
    abstract val jobDao: JobDao
    
    companion object {
        const val DATABASE_NAME = "InterviewAlarm_db"
    }
}
