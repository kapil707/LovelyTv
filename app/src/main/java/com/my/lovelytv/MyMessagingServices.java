package com.my.lovelytv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/*import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;*/
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
public class MyMessagingServices
{

}/*
public class MyMessagingServices extends FirebaseMessagingService {
    public static final String ANDROID_CHANNEL_ID = "com.my.lovelytv";
    private Noti mNotificationUtils;
    String mainurl = "http://drdmail.xyz/";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Toast.makeText(this, remoteMessage.getNotification().getTitle(), Toast.LENGTH_SHORT).show();

        String id = remoteMessage.getData().get("id");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        //Toast.makeText(this, image, Toast.LENGTH_SHORT).show();

        /*message = message.replace("<br>", "\n");
        message = message.replace("<br/>", "\n");
        message = message.replace("<br />", "\n");
        /*message = message.replace("<b>", "*");
        message = message.replace("</b>", "*");*/

        /*Intent intent = new Intent(this, ForegroundService.class);
        ContextCompat.startForegroundService(this, intent);
        mGPS_info();//18004194000*//*
        addNotification(Integer.parseInt(id), title,message);
    }

    GPSTracker mGPS;
    double latitude1, longitude1;
    String latitude,longitude;
    public void mGPS_info()
    {
        mGPS = new GPSTracker(this);
        mGPS.getLocation();

        latitude1 = mGPS.getLatitude();
        longitude1 = mGPS.getLongitude();

        latitude  = String.valueOf(latitude1);
        longitude = String.valueOf(longitude1);
    }

    private void addNotification(int myid, String title, String message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            contentView.setImageViewResource(R.id.image, R.drawable.ok);
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.text,Html.fromHtml(message));

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.drawable.ok) //set icon for notification
                            .setContent(contentView)//this is notification message
                            .setAutoCancel(true) // makes auto cancel of notification
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(soundUri);

            Intent notificationIntent = null;
            notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(myid, builder.build());
        } else {
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            contentView.setImageViewResource(R.id.image, R.drawable.ok);
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.text, Html.fromHtml(message));

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.drawable.ok) //set icon for notification
                            .setContent(contentView)//this is notification message
                            .setAutoCancel(true) // makes auto cancel of notification
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(soundUri)
                            .setPriority(Notification.PRIORITY_DEFAULT); //set priority of notification

            Intent notificationIntent = null;
            notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(myid, builder.build());

        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}*/
