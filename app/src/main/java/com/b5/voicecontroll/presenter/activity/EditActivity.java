package com.b5.voicecontroll.presenter.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Locale;
import com.b5.voicecontroll.R;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    private String startTime = "startTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    public void startState(View view) {
        onBackPressed();
    }

    public void decideTimes(View view) {
        // 現在時刻を取得
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 時間選択ダイアログの生成
        TimePickerDialog timepick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {// 設定 ボタンクリック時の処理
                        // 時と分を文字列として結合
                        String timeStr = String.format(Locale.US, "%d:%d", hourOfDay, minute);

                        // 紐づけ
                        TextView setTime = (TextView)findViewById(R.id.time_decision); //
                        setTime.setText(timeStr);
                    }
                },
                hour,
                minute,
                true);

        // 表示
        timepick.show();




    }
}


