package com.android.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class AlarmClockManager extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                AlarmClockClass alarmObject = intent.getParcelableExtra("alarmObject");
                MediaPlayer mp = MediaPlayer.create(context, alarmObject.getAudioUri());
                mp.start();
            }
        };
        r.run();
    }
}
