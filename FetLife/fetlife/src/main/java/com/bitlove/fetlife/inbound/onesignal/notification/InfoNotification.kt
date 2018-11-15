package com.bitlove.fetlife.inbound.onesignal.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bitlove.fetlife.FetLifeApplication
import com.bitlove.fetlife.R
import com.bitlove.fetlife.event.NewMessageEvent
import com.bitlove.fetlife.model.service.FetLifeApiIntentService
import com.bitlove.fetlife.notification.NotificationParser
import com.bitlove.fetlife.view.screen.BaseActivity
import com.bitlove.fetlife.view.screen.resource.ConversationsActivity
import com.bitlove.fetlife.view.screen.resource.MessagesActivity
import com.bitlove.fetlife.view.screen.resource.TurboLinksViewActivity
import org.json.JSONObject

class InfoNotification(notificationType: String, notificationIdRange: Int, title: String, message: String, launchUrl: String, mergeId: String?, collapseId: String?, additionalData: JSONObject, preferenceKey: String?) : OneSignalNotification(notificationType, notificationIdRange, title, message, launchUrl, mergeId, collapseId, additionalData, preferenceKey) {

    override fun getNotificationChannelName(context: Context): String? {
        return context.getString(R.string.settings_title_notification_info_enabled)
    }

    override fun getNotificationChannelDescription(context: Context): String? {
        return context.getString(R.string.settings_summary_notification_info_enabled)
    }

    override fun getSummaryTitle(notificationCount: Int, context: Context): String? {
        return context.resources.getQuantityString(R.plurals.noification_summary_title_info, notificationCount, notificationCount)
    }

    override fun getSummaryText(notificationCount: Int, context: Context): String? {
        return context.getString(R.string.noification_summary_text_info)
    }

    override fun getNotificationIntent(oneSignalNotification: OneSignalNotification, context: Context, order: Int): PendingIntent? {
        val contentIntent = if (launchUrl != null) {
            Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_RECEIVER_FOREGROUND)
                data = Uri.parse(launchUrl)
            }
        } else {
            TurboLinksViewActivity.createIntent(context, "notifications", context.getString(R.string.title_activity_notifications), true, null, true).apply {
                addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                putExtra(BaseActivity.EXTRA_NOTIFICATION_SOURCE_TYPE, oneSignalNotification.notificationType)
                putExtra(BaseActivity.EXTRA_NOTIFICATION_MERGE_ID, oneSignalNotification.mergeId)
            }
        }
        return PendingIntent.getActivity(context,order,contentIntent,PendingIntent.FLAG_CANCEL_CURRENT)
    }
}