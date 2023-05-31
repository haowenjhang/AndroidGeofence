package com.wen.geofencelib

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver : BroadcastReceiver() {

    var Tag:String  = "MyLog"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(Tag, " onReceive!！")

        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(Tag, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            sendNotification(context!!, intent!!)

            Log.e(Tag, "我又進來了")
        }

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            sendNotification(context!!, intent!!)

            Log.e(Tag, "我又出去了")
        }
    }


    private fun sendNotification(context: Context, intent: Intent) {

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent!!.putExtra("Intent", "Geofence")

        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, "GeofenceBroadcastReceiver_id")
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setContentIntent(pendingIntent)
            .setContentTitle("通知標題")
            .setContentText("通知內文")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Geofence"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("GeofenceBroadcastReceiver_id", name, importance)
            channel.description = "Geofence"
            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            )
            notificationManager!!.createNotificationChannel(channel)
            notificationManager.notify(10000, builder.build())
        }
    }
}