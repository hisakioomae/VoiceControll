package com.b5.voicecontroll.presenter.presenter.entity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import com.b5.voicecontroll.presenter.presenter.service.AudioService;

public class TimeReceive extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "指定時間です", Toast.LENGTH_SHORT).show();
        Intent serviceIntent = new Intent(context, AudioService.class);
        context.startService(serviceIntent);

    }
}
