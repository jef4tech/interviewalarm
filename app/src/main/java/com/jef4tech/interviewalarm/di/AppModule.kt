package com.jef4tech.interviewalarm.di

import android.app.AlarmManager
import android.content.Context
import androidx.room.Room
import com.jef4tech.interviewalarm.data.local.InterviewAlarmDatabase
import com.jef4tech.interviewalarm.data.local.JobDao
import com.jef4tech.interviewalarm.data.repository.JobRepositoryImpl
import com.jef4tech.interviewalarm.domain.repository.JobRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InterviewAlarmDatabase {
        return Room.databaseBuilder(
            context,
            InterviewAlarmDatabase::class.java,
            InterviewAlarmDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideJobDao(database: InterviewAlarmDatabase): JobDao {
        return database.jobDao
    }

    @Provides
    @Singleton
    fun provideJobRepository(dao: JobDao): JobRepository {
        return JobRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}
