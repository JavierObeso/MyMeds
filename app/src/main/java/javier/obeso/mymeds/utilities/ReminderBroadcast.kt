package javier.obeso.mymeds.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javier.obeso.mymeds.R

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var builder = NotificationCompat.Builder(context!!, "notificacionMyMeds")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Hey! Es hora de tu dosis de medicina!")
            .setContentText("Recuerda cuidar de ti!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(123, builder.build())
        }

    }

}