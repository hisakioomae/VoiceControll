package com.b5.voicecontroll.presenter.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.b5.voicecontroll.R;

import java.util.Calendar;

public class editActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    public void startState(View view) {
        Intent ssIntent = new Intent(this, MainActivity.class);
        startActivity(ssIntent);
    }

    public void DecideTimes(View view) {
        // 現在時刻を取得
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 時間選択ダイアログの生成
        TimePickerDialog timepick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {
                        // 設定 ボタンクリック時の処理
                    }
                },
                hour,
                minute,
                true);

        // 表示
        timepick.show();
    }
}


