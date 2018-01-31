package com.b5.voicecontroll.presenter.presenter.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

public class AudioService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_SHOW_UI); //着信音量を0に設定
        Toast.makeText(this, "設定時間になりました", Toast.LENGTH_SHORT).show();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
