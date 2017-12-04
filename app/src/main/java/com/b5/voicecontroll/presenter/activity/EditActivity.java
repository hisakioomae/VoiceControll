package com.b5.voicecontroll.presenter.activity;

import android.app.TimePickerDialog;
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
    @BindView(R.id.start_time) TextView setStartTime;
    @BindView(R.id.end_time) TextView setEndTime;
    private String setting_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
    }


    /*
    キャンセルボタンをクリックでMainActivityに戻る
     */
    @OnClick(R.id.cancel_button)
    public void startState(View view){
        onBackPressed();
    }


    @OnClick(R.id.start_time)
    public void decideStartTimes(View view) {
        // 時間選択ダイアログの生成
        TimePickerDialog timePick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {// 設定 ボタンクリック時の処理
                        if (minute < 10) {
                            // 時と分を文字列として結合
                            setting_time = "%d時 0%d分";
                        } else {
                            setting_time = "%d時 %d分";
                        }
                        // "%d : %d"をxmlのend_timeに格納
                        setStartTime.setText(String.format(Locale.US, setting_time, hourOfDay, minute));
                    }
                },
                hour,
                minute,
                true);
        timePick.show();

    }


    @OnClick(R.id.end_time)
    public void decideEndTimes(View view) {
        // 時間選択ダイアログの生成
        TimePickerDialog timePick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {// 設定 ボタンクリック時の処理
                        if (minute < 10) {
                            // 時と分を文字列として結合
                            setting_time = "%d時 0%d分";
                        } else {
                            setting_time = "%d時 %d分";
                        }
                        // "%d : %d"をxmlのend_timeに格納
                        setEndTime.setText(String.format(Locale.US, setting_time, hourOfDay, minute));
                    }
                },
                hour,
                minute,
                true);
        timePick.show();

    }
}
