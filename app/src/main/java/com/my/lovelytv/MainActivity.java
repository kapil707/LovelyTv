package com.my.lovelytv;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Toast;

/*import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;*/

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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public String main_url="";
    String page_url="http://skychannelnetwork.in/newapi/api_1_1.php";
    String android_id = "";
    String result ="";
    final Context context = this;
    String firebase_token = "";

    UserSessionManager session;
    String user_name1 = "";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        user_name1 = user.get(UserSessionManager.KEY_USERNAME);

        if (session.checkLogin()) {
            finish();
        }
        else {

            /*Intent intent = new Intent(this, ForegroundService.class);
            ContextCompat.startForegroundService(context, intent);

            ComponentName componentName = new ComponentName(this, MyJobService.class);
            JobInfo info = new JobInfo.Builder(123, componentName)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .setPeriodic(5 * 60 * 1000)
                    .build();
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                //Log.d(TAG, "Job scheduled");
                //Toast.makeText(getApplicationContext(), "Job scheduled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(), "Job scheduling failed", Toast.LENGTH_LONG).show();
                //Log.d(TAG, "Job scheduling failed");
            }

            /*Intent intent = new Intent(this, MyLocationService.class);
            startService(intent);

            /*startService(new Intent(getApplicationContext(),MyService.class));*/

            /*Intent intent = new Intent(this, ForegroundService.class);
            ContextCompat.startForegroundService(context, intent);*/

            /*JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(11, new ComponentName(this, MyJobService.class))
                    // only add if network access is required
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build();
            jobScheduler.schedule(jobInfo);*/

            try {
                this.getSupportActionBar().hide();
            } catch (NullPointerException e) {
            }

            android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            //new chemist_info_for_delivery_boy().execute();

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean agreed = sharedPreferences.getBoolean("agreed", false);
            if (!agreed) {
                new AlertDialog.Builder(context)
                        .setTitle("Copyright")
                        .setPositiveButton("Agreed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("agreed", true);
                                editor.commit();
                                new chemist_info_for_delivery_boy().execute();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setMessage("This application does not stream any of the channels included in this application, all the streaming links are from third party website available freely on the internet. We're just giving way to stream and all content is the copyright of their owner.")
                        .show();
            }
            if (agreed == true) {
                new chemist_info_for_delivery_boy().execute();
            }
        /*new Timer().schedule(new TimerTask() {
            public void run() {
                Intent in = new Intent();
                in.setClass(MainActivity.this, Home_page.class);
                in.putExtra("android_id", android_id);
                startActivity(in);
                finish();
            }
        }, 2000);*/
        }
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Lovely Tv");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.ok2));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }
    private class chemist_info_for_delivery_boy extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            result = "";
            InputStream isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(page_url);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-11-22"));
                nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));
                nameValuePairs.add(new BasicNameValuePair("android_id", android_id));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                //Log.e("log_tag","Error in connection"+e.toString());
                //tv.setText("couldn't connect to the database");
                //mProgressDialog.dismiss();
                //user_alert = "Check your internet";
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
            return null;
        }
        @Override
        protected void onPostExecute(Void args)
        {
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String ok = jsonObject.getString("ok");

                    //Toast.makeText(getApplicationContext(), id+"--"+ok+"--"+android_id, Toast.LENGTH_LONG).show();

                    Intent in = new Intent();
                    in.setClass(MainActivity.this, Home_page.class);
                    in.putExtra("id", id);
                    in.putExtra("android_id", android_id);
                    startActivity(in);
                    finish();
                }
            }catch (Exception e) {

            }
        }
    }
}
