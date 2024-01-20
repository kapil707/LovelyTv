package com.my.lovelytv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

public class Player_mp4_mkv extends AppCompatActivity {

    private SimpleExoPlayer mSimpleExoPlayer;

    private SimpleExoPlayerView mSimpleExoPlayerView;

    private Handler mMainHandler;
    private AdaptiveVideoTrackSelection.Factory mAdaptiveTrackSelectionFactory;
    private TrackSelector mTrackSelector;
    private LoadControl mLoadControl;
    private DefaultBandwidthMeter mBandwidthMeter;
    private DataSource.Factory mDataSourceFactory;
    private SimpleCache mSimpleCache;
    private DataSource.Factory mFactory;
    private MediaSource mVideoSource;
    private LoopingMediaSource mLoopingMediaSource;
    private ProgressBar mProgressBar;

    String page_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player_mp4_mkv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.menu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        View view = getSupportActionBar().getCustomView();

        Intent in = getIntent();
        page_url = in.getStringExtra("url");

        String name = in.getStringExtra("name");
        TextView action_bar_title1 = (TextView) findViewById(R.id.action_bar_title);
        action_bar_title1.setText(name);

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout cast_LinearLayout = findViewById(R.id.cast_LinearLayout);
        cast_LinearLayout.setVisibility(View.GONE);

        mSimpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // See whether the player view wants to handle media or DPAD keys events.
        mSimpleExoPlayerView.showController();
        return mSimpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    private void playMedia() {
        mBandwidthMeter = new DefaultBandwidthMeter();
        mAdaptiveTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(mBandwidthMeter);
        mTrackSelector = new DefaultTrackSelector(mAdaptiveTrackSelectionFactory);

        mLoadControl = new DefaultLoadControl();

        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, mTrackSelector, mLoadControl);

        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Player_mp4_mkv.this,
                Util.getUserAgent(Player_mp4_mkv.this, "com.exoplayerdemo"), bandwidthMeter);

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(page_url),
                dataSourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        mLoopingMediaSource = new LoopingMediaSource(videoSource);

        mSimpleExoPlayer.prepare(videoSource);

        mSimpleExoPlayer.setPlayWhenReady(true);

        mSimpleExoPlayer.addListener(new ExoPlayer.EventListener() {


            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mSimpleExoPlayerView.showController();
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                //Toast.makeText(Player_mp4_mkv.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder adb = new AlertDialog.Builder(Player_mp4_mkv.this);
                adb.setTitle("Could not able to stream video");
                adb.setMessage("It seems that something is going wrong.\nPlease try again.");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish(); // take out user from this activity. you can skip this
                    }
                });
                AlertDialog ad = adb.create();
                ad.show();
            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        playMedia();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMedia();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMedia();
    }

    private void stopMedia() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
    }

    @Override
    public void onBackPressed() {
        showSystemUI();
        finish();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
