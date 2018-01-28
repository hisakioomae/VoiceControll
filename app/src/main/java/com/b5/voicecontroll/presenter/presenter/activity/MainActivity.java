package com.b5.voicecontroll.presenter.presenter.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.b5.voicecontroll.R;
import com.b5.voicecontroll.presenter.presenter.adapter.MyAdapter;
import com.b5.voicecontroll.presenter.presenter.entity.ListItem;
import com.b5.voicecontroll.presenter.presenter.entity.TimeReceive;
// TODO: Dialog追加予定
import android.support.v4.app.Fragment;

import com.b5.voicecontroll.presenter.presenter.fragment.AlertDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final int ADD_CODE = 1;  // 新規追加時のrequestCode
    private static final int EDIT_CODE = 2;  // 編集時のrequestCode

    // TODO: 追加予定ダイアログ用
    private static final int FRAGMENT_CODE = 3;  // AlertDialog呼び出し時のrequestCode
    private DialogFragment dialogFragment;
    private FragmentManager fragmentManager;

    private MyAdapter adapter;
    int times[] = {0, 0, 0, 0};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<ListItem> data = new ArrayList<>();
        adapter = new MyAdapter(this, data);
        final ListView list = findViewById(R.id.list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = (ListItem) parent.getItemAtPosition(position);
                times = item.getTimeBox();
                timeEdit(position);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //　TODO:確認用ダイアログ追加予定
                //dialogFragment = new  AlertDialogFragment();
                //fragmentManager = getSupportFragmentManager();
                // 消去確認ダイアログの表示
//                dialogFragment.show(fragmentManager, "test alert dialog");

                ListItem item = (ListItem) parent.getItemAtPosition(position);
                adapter.deleteData(item);
                return true;
            }
        });
    }


    @Override
    public void onRestart() {
        super.onRestart();
        Arrays.fill(times, 0);
        for (int i = 0; i < adapter.getCount(); i++) {
            sendTimeProcess(i);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case (ADD_CODE):
                if (resultCode == RESULT_OK) {
                    //timeBox/dayにそれぞれEditActivityから受け取った設定時間の配列/曜日の文字列を格納
                    int timeBox[] = intent.getIntArrayExtra("return_times");
                    String day = intent.getStringExtra("chosen_day");
                    // 配列/文字列の内容をListItemオブジェクトに詰め替え
                    ListItem item = new ListItem();
                    item.setId((new Random()).nextLong());
                    item.setTimes(timeBox);
                    item.setDay(day);
                    adapter.setData(item);
                    break;
                }
            case (EDIT_CODE):
                if (resultCode == RESULT_OK) {
                    int position = intent.getIntExtra("list_position", 0);
                    int timeBox[] = intent.getIntArrayExtra("return_times");
                    String day = intent.getStringExtra("chosen_day");
                    ListItem item = new ListItem();
                    item.setId((new Random()).nextLong());
                    item.setTimes(timeBox);
                    item.setDay(day);
                    adapter.changeData(position, item);
                    break;
                }
        }
    }


    /**
     * 編集ボタンタップでEditActivityに遷移
     *
     * @param view activity_main.xml
     */
    public void timeAdd(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("edit_times", times);
        startActivityForResult(intent, ADD_CODE);
    }

    /**
     * ListViewの項目長押しでで編集画面(EditActivity)に遷移
     *
     * @param position 長押しした項目の位置
     */
    public void timeEdit(int position) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("edit_times", times);
        intent.putExtra("list_position", position);
        startActivityForResult(intent, EDIT_CODE);
    }

    /**
     * AlarmManagerに指定時間にTimeReceiveを処理するように登録
     *
     * @param position 設定時間のListViewの要素の位置
     */
    public void sendTimeProcess(int position) {
        Intent intent = new Intent(getApplicationContext(), TimeReceive.class);
        intent.setType(String.valueOf(position));
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        final ListView list = findViewById(R.id.list_view);
        ListItem item = (ListItem) list.getItemAtPosition(position);
        System.out.println(item.getTimeBox()[0]);

        // 現在の時刻をcalendarにセット
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // ターゲット時刻をcalendar_targetにセット
        Calendar calendar_target = Calendar.getInstance();
        calendar_target.setTimeInMillis(System.currentTimeMillis());
        calendar_target.set(Calendar.HOUR_OF_DAY, item.getTimeBox()[0]);
        calendar_target.set(Calendar.MINUTE, item.getTimeBox()[1]);

        // ターゲット時刻が何秒後かを追加
        calendar.add(Calendar.SECOND, returnSecond(calendar, calendar_target));

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        System.out.println(calendar.getTimeInMillis());
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    /**
     * 設定時刻までの時間情報をSECONDフィールドに変換する
     *
     * @param calendar        現在の時間情報を含んだCalendar型変数
     * @param calendar_target 設定時間の情報を含んだCalendar型変数
     * @return 設定時刻までの秒数
     */
    public int returnSecond(Calendar calendar, Calendar calendar_target) {
        int hourDiff = Math.abs(calendar_target.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY));
        int minuteDiff = Math.abs(calendar_target.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE));

        // 設定時間のHOURがまだきていない時
        if (calendar.get(Calendar.HOUR) > calendar_target.get(Calendar.HOUR)) {
            if (calendar.get(Calendar.MINUTE) > calendar_target.get(Calendar.MINUTE)) {
                // MINUTEの繰り上がりを考慮
                minuteDiff = -(minuteDiff);
            }
        }
        // 過ぎている場合、１日後に設定
        else if (calendar.get(Calendar.HOUR) < calendar_target.get(Calendar.HOUR)) {
            hourDiff = 24 - hourDiff;
            if (calendar.get(Calendar.MINUTE) > calendar_target.get(Calendar.MINUTE)) {
                // MINUTEの繰り上がりを考慮
                minuteDiff = -(minuteDiff);
            }
        }
        // 同じ値の時
        else {
            if (calendar.get(Calendar.MINUTE) >= calendar_target.get(Calendar.MINUTE)) {
                hourDiff = 24;
                // MINUTEの繰り上がりを考慮
                minuteDiff = -(minuteDiff);
            } else {
                hourDiff = 0;
            }
        }

        // TODO: デバックで利用
        System.out.println((hourDiff * 3600) + (minuteDiff * 60) - calendar.get(Calendar.SECOND) + "秒後");

        return (hourDiff * 3600) + (minuteDiff * 60) - calendar.get(Calendar.SECOND) - 2;
    }

}
