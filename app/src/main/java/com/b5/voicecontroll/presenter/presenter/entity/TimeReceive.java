package com.b5.voicecontroll.presenter.presenter.entity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TimeReceive extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "5秒後です", Toast.LENGTH_SHORT).show();
    }
}
