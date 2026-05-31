package com.kndrd.android.notification

// KndrdFcmService requires Firebase to be configured.
// Steps to enable:
// 1. Create a Firebase project at console.firebase.google.com
// 2. Add an Android app with package name "com.kndrd.android"
// 3. Download google-services.json and place it in app/
// 4. Uncomment alias(libs.plugins.google.services) in both build.gradle.kts files
// 5. Uncomment Firebase dependencies in app/build.gradle.kts
// 6. Uncomment this class and the service entry in AndroidManifest.xml

/*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kndrd.android.R
import com.kndrd.android.core.data.preferences.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class KndrdFcmService : FirebaseMessagingService() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            userPreferences.saveFcmToken(token)
            // TODO: send token to backend
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title = remoteMessage.notification?.title ?: return
        val body = remoteMessage.notification?.body ?: return
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "kndrd_chat"
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "Chat Messages", NotificationManager.IMPORTANCE_HIGH)
            )
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
*/
