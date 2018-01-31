package com.b5.voicecontroll.presenter.presenter.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;

import com.b5.voicecontroll.R;
import com.b5.voicecontroll.presenter.presenter.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditActivity extends AppCompatActivity {
    private String setting_time;
    private int timeBox[] = new int[4];
    private int listPosition = 0;
    int setHour, setMinute = 0;
    @BindView(R.id.start_time)
    TextView setStartTime;
    @BindView(R.id.end_time)
    TextView setEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        timeBox = intent.getIntArrayExtra("edit_times");
        listPosition = intent.getIntExtra("list_position", 0);
        setStartTime.setText(String.format(Locale.US, "%d:%d", timeBox[0], timeBox[1]));
        setEndTime.setText(String.format(Locale.US, "%d:%d", timeBox[2], timeBox[3]));
    }

    /**
     * キャンセルボタンタップでMainActivityに戻る
     *
     * @param view activity_edit.xml
     */
    @OnClick(R.id.cancel_button)
    public void backState(View view) {
        onBackPressed();
    }

    /**
     * 開始時間のテキストをタップで時間ダイアログを表示
     *
     * @param view activity_edit.xml
     */
    @OnClick(R.id.start_time)
    public void decideStartTimes(View view) {
        setHour = timeBox[0];
        setMinute = timeBox[1];
        setDialogTime(setStartTime, 0);
    }

    /**
     * 終了時間のテキストをタップで時間ダイアログを表示
     *
     * @param view activity_edit.xml
     */
    @OnClick(R.id.end_time)
    public void decideEndTimes(View view) {
        setHour = timeBox[2];
        setMinute = timeBox[3];
        setDialogTime(setEndTime, 1);
    }

    /**
     * 時間ダイアログで設定した時間を各textViewに反映
     *
     * @param textView 反映させるTextView
     * @param flag     開始 or 終了の判定のためのflag
     */
    private void setDialogTime(final TextView textView, final int flag) {
        TimePickerDialog timePick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view,
                                          int hourOfDay, int minute) {// 設定 ボタンクリック時の処理
                        // timeBoxに開始/終了時間を格納
                        switch (flag) {
                            case (0):
                                timeBox[0] = hourOfDay;
                                timeBox[1] = minute;
                                break;
                            case (1):
                                timeBox[2] = hourOfDay;
                                timeBox[3] = minute;
                                break;
                        }
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
                setHour,
                setMinute,
                true);
        timePick.show();
    }

    /**
     * 保存ボタンを押したときに指定した時間帯をMainActivityに渡す
     *
     * @param view activity_edit.xml
     */
    @OnClick(R.id.save_button)
    public void returnSettingTime(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        //days_spinnerオブジェクトから選択アイテムの取得
        Spinner spinner = findViewById(R.id.days_spinner);
        String item = (String) spinner.getSelectedItem();
        intent.putExtra("return_times", timeBox);
        intent.putExtra("chosen_day", item);
        intent.putExtra("list_position", listPosition);
        setResult(RESULT_OK, intent);
        finish();
    }

}
