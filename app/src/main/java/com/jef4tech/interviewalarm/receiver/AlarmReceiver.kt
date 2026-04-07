package com.jef4tech.interviewalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val jobId = intent.getStringExtra("JOB_ID") ?: return
        val jobTitle = intent.getStringExtra("JOB_TITLE") ?: "Job Interview"
        val company = intent.getStringExtra("COMPANY") ?: "Company"

        // Create and show Notification
        val notificationId = jobId.hashCode()
        val channelId = "interview_reminders"

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // System fallback icon
            .setContentTitle("Upcoming Interview: $company")
            .setContentText("Your interview for $jobTitle is starting in 30 minutes!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        try {
            // Context needs POST_NOTIFICATIONS permission logically handled before reaching this
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
