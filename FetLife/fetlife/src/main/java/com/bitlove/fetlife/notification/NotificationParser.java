package com.bitlove.fetlife.notification;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationParser {

    public static final String JSON_FIELD_STRING_LAUNCHURL = "launchURL";
    public static final String JSON_FIELD_STRING_TITLE = "title";
    public static final String JSON_FIELD_STRING_GROUP = "grp";
//    public static final String JSON_FIELD_JSONOBJECT_ADDITIONA_DATA = "a";
//    public static final String JSON_FIELD_STRING_U = "u";
//    public static final String JSON_FIELD_STRING_ID = "i";

    public static final String JSON_FIELD_STRING_CONVERSATIONID = "conversation_id";
    public static final String JSON_FIELD_STRING_NICKNAME = "nickname";

    public static final String JSON_FIELD_STRING_COLLAPSE_ID = "collapse_id";
    public static final java.lang.String JSON_FIELD_STRING_VERSION = "version";

    private static final String JSON_FIELD_STRING_TYPE = "type";
    private static final String JSON_FIELD_STACKED_NOTIFICATION = "stacked_notifications";

    public static final String JSON_VALUE_GROUP_INFO = "info";
    public static final String JSON_VALUE_GROUP_FETLIFE = "fetlife";
    public static final String JSON_VALUE_GROUP_MESSAGE = "messages";

//    public OneSignalNotification parseNotification(String message, JSONObject notificationJson) {
//
//        String url = notificationJson.optString(JSON_FIELD_STRING_U);
//        String id = notificationJson.optString(JSON_FIELD_STRING_ID);
//        JSONObject additionalData = notificationJson.optJSONObject(JSON_FIELD_JSONOBJECT_ADDITIONA_DATA);
//
//        return parseNotification(message, url, additionalData, id);
//    }

    public OneSignalNotification parseNotification(String title, String message, String launchUrl, JSONObject additionalData, String id, String group) {

        JSONArray subNotifications;
        if (additionalData != null && (subNotifications = additionalData.optJSONArray(JSON_FIELD_STACKED_NOTIFICATION)) != null) {
            List<OneSignalNotification> subNotificationList = new ArrayList<>(subNotifications.length());
            for (int i = 0; i < subNotifications.length(); i++) {
                JSONObject notificationObject = subNotifications.optJSONObject(i);
                String subTitle = notificationObject.optString(JSON_FIELD_STRING_TITLE);
                String subMessage = notificationObject.optString(JSON_FIELD_STRING_TITLE);
                String subLaunchUrl = notificationObject.optString(JSON_FIELD_STRING_TITLE);
                String subGroup = notificationObject.optString(JSON_FIELD_STRING_GROUP);
                OneSignalNotification subNotification = parseNotification(subTitle, subMessage, subLaunchUrl, notificationObject, null, subGroup);
                if (group == null || group.trim().length() == 0) {
                    group = subGroup;
                }
                subNotificationList.add(subNotification);
            }
            StackedNotification stackedNotification = new StackedNotification(title, message, launchUrl, additionalData, id, group, subNotificationList);
            return stackedNotification;
        }

        String type = additionalData != null ? additionalData.optString(JSON_FIELD_STRING_TYPE).toLowerCase() : "";

        switch (type) {
            case "info":
                return new InfoNotification(title, message, launchUrl, additionalData, id, group);
            case "version":
                return new VersionNotification(title, message, launchUrl, additionalData, id, group);
            case "conversation_created":
            case "message_created":
                return new MessageNotification(title, message, launchUrl, additionalData, id, group);
//            case "friendship_request_created":
//                return new FriendRequestNotification(title, message, launchUrl, additionalData, id, group);
//            case "friendship_request_accepted":
//                return new FriendAddedNotification(title, message, launchUrl, additionalData, id, group);
//            case "friend_deleted":
//                return new FriendDeletedNotification(title, message, launchUrl, additionalData, id, group);
//            case "conversation_archived":
//                return new ConversationArchivedNotification(message, launchUrl, additionalData, id, group);
            default:
                if (title != null || message != null) {
                    return new InfoNotification(title, message, launchUrl, additionalData, id, group);
                } else {
                    return new UnknownNotification(title, message, launchUrl, additionalData, id, group);
                }
        }

    }

}
