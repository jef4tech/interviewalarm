package com.jef4tech.interviewalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jef4tech.interviewalarm.domain.repository.JobRepository
import com.jef4tech.interviewalarm.domain.usecase.ScheduleInterviewAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var jobRepository: JobRepository

    @Inject
    lateinit var scheduleInterviewAlarmUseCase: ScheduleInterviewAlarmUseCase

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            
            // Leveraging IO dispatcher so we don't block the main thread directly
            CoroutineScope(Dispatchers.IO).launch {
                val upcomingJobs = jobRepository.getUpcomingInterviews().firstOrNull()
                
                upcomingJobs?.forEach { job ->
                    // Re-register alarms securely
                    scheduleInterviewAlarmUseCase(job)
                }
            }
        }
    }
}
