package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, id: Long) {

    val transferIntent = Intent(applicationContext, DetailActivity::class.java)
    transferIntent.putExtra("id_extra", id)
    val transferPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        transferIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )

    .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentText(applicationContext.getString(R.string.notification_description))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.button_string),
            transferPendingIntent
        )

    notify(id.toInt(), builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}