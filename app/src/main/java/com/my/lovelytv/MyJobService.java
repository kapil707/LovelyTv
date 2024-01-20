package com.my.lovelytv;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@SuppressLint("NewApi")
public class MyJobService extends JobService {

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        // runs on the main thread, so this Toast will appear

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(this,ForegroundService.class);
            startService(intent);
            //Toast.makeText(this, "test1", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent serviceIntent = new Intent(this, ForegroundService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            ContextCompat.startForegroundService(this, serviceIntent);
            //Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
        }
        // perform work here, i.e. network calls asynchronously

        // returning false means the work has been done, return true if the job is being run asynchronously
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        // if the job is prematurely cancelled, do cleanup work here
        //Toast.makeText(this, "onStopJob", Toast.LENGTH_SHORT).show();
        // return true to restart the job
        return false;
    }
}
