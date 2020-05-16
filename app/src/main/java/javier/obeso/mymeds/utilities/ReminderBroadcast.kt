package javier.obeso.mymeds.utilities

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javier.obeso.mymeds.LoginActivity
import javier.obeso.mymeds.R
import kotlin.random.Random

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var requestCode = Random.nextInt(0, 20000)
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, requestCode, intent, 0)

        var builder = NotificationCompat.Builder(context!!, "notificacionMyMeds")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Hey! Es hora de tu dosis de medicina!")
            .setContentText("Recuerda cuidar de ti!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(123, builder.build())
        }

    }
}