package com.my.lovelytv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForegroundService extends Service {

    String result = "";
    String mainurl = "", page_url1 = "";
    UserSessionManager session;
    String user_name1 = "", user_phone1 = "", device_id = "",firebase_token="";


    private static long UPDATE_INTERVAL = 20 * 1000;
    private final Handler handler_myservice_page = new Handler();
    private Noti mNotificationUtils;
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
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    @Override
    public void onCreate() {
        super.onCreate();

        //Toast.makeText(getApplicationContext(), "Start ForegroundService", Toast.LENGTH_LONG).show();
        try {

            MainActivity ma = new MainActivity();
            page_url1 = "http://112.196.183.23/rkssoft/api_track/update_live_track_user/post";

            session = new UserSessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();
            user_name1 = user.get(UserSessionManager.KEY_USERNAME);
            user_phone1 = user.get(UserSessionManager.KEY_PHONE);
            device_id = user.get(UserSessionManager.KEY_DEVICEID);
            firebase_token = user.get(UserSessionManager.KEY_FIREBASE);
            handler_myservice_page.postDelayed(myservice_page, UPDATE_INTERVAL);

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "myservice_01" + e.toString());
        }
    }

    private Runnable myservice_page = new Runnable() {
        public void run() {
            try {
                handler_myservice_page.postDelayed(myservice_page, UPDATE_INTERVAL);
                mGPS_info();
                new update_live_track_user().execute();
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "myservice_03" + e.toString());
                Toast.makeText(getApplicationContext(), "myservice_03", Toast.LENGTH_LONG).show();
            }
        }
    };

    private class update_live_track_user extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            result = "";
            InputStream isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url1);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-kavita-zxy"));

                nameValuePairs.add(new BasicNameValuePair("user_name1", user_name1));
                nameValuePairs.add(new BasicNameValuePair("user_phone1", user_phone1));
                nameValuePairs.add(new BasicNameValuePair("device_id", device_id));
                nameValuePairs.add(new BasicNameValuePair("firebase_token", firebase_token));

                nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
                nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                isr.close();
                result = stringBuilder.toString();
            } catch (Exception e) {
                // TODO: handle exception
                //mProgressDialog.dismiss();
            }
            try {
            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            String toast_msg = "0";
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    toast_msg = jsonObject.getString("toast_msg");
                }
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("log_tag", "Error parsing data"+e.toString());
                //Toast.makeText(Login.this,"error json",Toast.LENGTH_SHORT).show();
            }
            if (toast_msg.equals("0")) {

            } else {
                result = "";
                //Toast.makeText(getApplicationContext(), toast_msg, Toast.LENGTH_SHORT).show();
                //addNotification("0101", toast_msg, toast_msg);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lovely Tv - Enjoy Entertainment")
                /*.setContentText("DRD Order Delivery App")*/
                .setSmallIcon(R.drawable.ok2)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "Service Stop", Toast.LENGTH_LONG).show();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("Autostart");
        broadcastIntent.setClass(this, BootCompleteReceiver.class);
        this.sendBroadcast(broadcastIntent);
    }
    /*@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onBind", Toast.LENGTH_LONG).show();
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Lovely Tv - Enjoy Entertainment",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}