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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {
    private final int hour = 0; //ダイアログ呼び出し時の初期時間・現在は0:00に設定
    private final int minute = 0;
    private String setting_time;
    private int time_box[] = new int[4];
    private int set_hour, set_minute;

    @BindView(R.id.start_time)
    TextView setStartTime;
    @BindView(R.id.end_time)
    TextView setEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
    }

    /**
     *キャンセルボタンをクリックでMainActivityに戻る
     */
    @OnClick(R.id.cancel_button)
    public void backState(View view) {
        onBackPressed();
    }

    @OnClick(R.id.start_time)
    public void decideStartTimes(View view) {
        setDialogTime(setStartTime, 0);
    }

    @OnClick(R.id.end_time)
    public void decideEndTimes(View view) {
       setDialogTime(setEndTime, 1);
    }

    /**
     *時間ダイアログで設定した時間を各textViewに反映
     */
    private void setDialogTime(final TextView textView, final int flag){
        TimePickerDialog timePick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {// 設定 ボタンクリック時の処理
                        // MainActivity受け渡しのための格納
                        switch(flag){
                            case(0):
                                time_box[0] = hourOfDay;
                                time_box[1] = minute;
                                break;
                            case(1):
                                time_box[2] = hourOfDay;
                                time_box[3] = minute;
                        }
                        set_hour = hourOfDay;
                        set_minute = minute;
                        if (minute < 10) {
                            // 時と分を文字列として結合
                            setting_time = "%d時 0%d分";
                        } else {
                            setting_time = "%d時 %d分";
                        }
                        // "%d : %d"をxmlのstart/end_timeに格納
                        textView.setText(String.format(Locale.US, setting_time, hourOfDay, minute));
                    }
                },
                hour,
                minute,
                true);
        timePick.show();
    }

    /**
     *保存ボタンを押したときに指定した時間帯をMainActivityに渡す
     */
    @OnClick(R.id.save_button)
    public  void returnSettingTime(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("return_times", time_box);
        setResult(RESULT_OK, intent);
        finish();
    }
}
