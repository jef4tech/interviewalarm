package com.jef4tech.interviewalarm.domain.usecase

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jef4tech.interviewalarm.domain.model.Job
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject

class ScheduleInterviewAlarmUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) {
    companion object {
        const val REMINDER_OFFSET_MINUTES = 30L
    }

    operator fun invoke(job: Job) {
        val interviewTime = job.interviewDateTime ?: return
        
        // Schedule reminder 30 mins prior to the interview
        val alarmTime = interviewTime.minusMinutes(REMINDER_OFFSET_MINUTES)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        if (alarmTime <= System.currentTimeMillis()) return

        val intent = Intent(context, Class.forName("com.jef4tech.interviewalarm.receiver.AlarmReceiver")).apply {
            putExtra("JOB_ID", job.id)
            putExtra("JOB_TITLE", job.title)
            putExtra("COMPANY", job.company)
            putExtra("INTERVIEW_TIME", alarmTime)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            job.id.hashCode(), // Unique ID for each job alarm
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent
                    )
                } else {
                    // Fallback to inexact alarm if permission not granted
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        alarmTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
