package app.adi_random.dealscraper.services.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import app.adi_random.dealscraper.MainComposeActivity
import app.adi_random.dealscraper.R
import app.adi_random.dealscraper.data.dto.notification.SetFcmTokenDto
import app.adi_random.dealscraper.services.api.NotificationApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class NotificationReceiverService : FirebaseMessagingService() {
    private val notificationApi by inject<NotificationApi>()
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            try {
                val apiResponse = notificationApi.updateToken(SetFcmTokenDto(token))
                val error = apiResponse.body()?.err ?: apiResponse.errorBody()?.string() ?: ""
                if (error != "") {
                    Log.e("NotificationReceiverService", "Error updating token: $error")
                }
            } catch (e: Exception) {
                Log.e("NotificationReceiverService", "Error updating token: ${e.message}")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        sendNotification()
    }

    private fun sendNotification() {
        val intent = Intent(this, MainComposeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("New Deal")
            .setContentText("We found a better deal on products that you usually buy")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}