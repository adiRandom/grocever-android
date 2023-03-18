package app.adi_random.dealscraper.services.notifications

import app.adi_random.dealscraper.data.dto.notification.SetFcmTokenDto
import app.adi_random.dealscraper.services.api.NotificationApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TokenService(private val notificationApi: NotificationApi) {
    fun updateFcmToken(scope: CoroutineScope) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                scope.launch(Dispatchers.IO) {
                    notificationApi.updateToken(SetFcmTokenDto(it.result!!))
                }
            }
        }
    }
}