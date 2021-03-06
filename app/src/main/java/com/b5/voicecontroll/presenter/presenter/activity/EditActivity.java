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
    private int timeBox[] = new int[2];
    private int listPosition = 0;
    int setHour, setMinute = 0;
    @BindView(R.id.start_time)
    TextView setStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        timeBox = intent.getIntArrayExtra("edit_times");
        listPosition = intent.getIntExtra("list_position", 0);
        Spinner spinner = findViewById(R.id.days_spinner);
        spinner.setSelection(setSpinner(intent.getStringExtra("select_day")));
        if (timeBox[1] < 10) {
            // 時と分を文字列として結合
            setStartTime.setText(String.format(Locale.US, "%d:0%d", timeBox[0], timeBox[1]));
        } else {
            setStartTime.setText(String.format(Locale.US, "%d:%d", timeBox[0], timeBox[1]));
        }
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
                        // timeBoxに開始時間を格納
                        timeBox[0] = hourOfDay;
                        timeBox[1] = minute;

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

    /**
     * Spinnerの初期値を設定
     *
     * @param day 　編集するListViewに現在選択されている繰り返し項目
     */
    public int setSpinner(String day) {
        int index = 0;
        if (day.equals("日曜日")) {
            index = 1;
        } else if (day.equals("月曜日")) {
            index = 2;
        } else if (day.equals("火曜日")) {
            index = 3;
        } else if (day.equals("水曜日")) {
            index = 4;
        } else if (day.equals("木曜日")) {
            index = 5;
        } else if (day.equals("金曜日")) {
            index = 6;
        } else if (day.equals("土曜日")) {
            index = 7;
        }
        return index;
    }

}
