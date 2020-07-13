package com.example.cats.utils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.cats.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayer {
    private static android.media.MediaPlayer mediaPlayer;
    private static Timer timer;
    private static ArrayList<Integer> playlist;
    private static int i = 0;
    private static boolean delayedPause = false;
    private static boolean volume = true;

    public static void create(final Context context){
        if(mediaPlayer == null) {
            playlist = new ArrayList<>();
            playlist.add(R.raw.song1);
            playlist.add(R.raw.song2);
            playlist.add(R.raw.song3);
            mediaPlayer = android.media.MediaPlayer.create(context, playlist.get(0));
            mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mp) {
                    i = (i+1) % 3;
                    mp.reset();
                    mp = android.media.MediaPlayer.create(context, playlist.get(i));
                    mp.start();
                    if(volume) mp.setVolume(1f, 1f);
                    else mp.setVolume(0f, 0f);
                }
            });
            mediaPlayer.start();

        }
    }

    public static void start(){
        if(mediaPlayer != null)
            mediaPlayer.setVolume(1f, 1f);
        volume = true;
    }

    public static void stop(){
        if(mediaPlayer != null)
            mediaPlayer.setVolume(0f, 0f);
        volume = false;
    }

    public static void release(){
        if(mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
    }

    public static void setDelayedPause(){
        delayedPause = true;
    }

    public static void resetDelayedPause(){
        delayedPause = false;
    }

    public static void delayedPause(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(delayedPause){
                    stop();
                }
            }
        }, 500);

    }
}
