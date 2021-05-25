package com.example.youtubeplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeActivity extends YouTubeBaseActivity {
    private String videoId;
    private static final String TAG = "YoutubeActivity";

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        mYouTubePlayerView = findViewById(R.id.youtubePlayerView);

        Log.d(TAG, "onCreate: Starting.");
        if (getIntent().getExtras() != null) {
            videoId = getIntent().getStringExtra(MainActivity.EXTRA_ID);
        }

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "initializePlayer(): Done initializing.");
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "initializePlayer(): Failed to initialize.");

            }
        };

        initializePlayer();
    }

    private void initializePlayer() {
        Log.d(TAG, "initializePlayer(): initializing YouTube Player");
        mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(), mOnInitializedListener);
    }
}