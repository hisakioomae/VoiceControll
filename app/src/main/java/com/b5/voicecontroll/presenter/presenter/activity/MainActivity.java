package com.b5.voicecontroll.presenter.presenter.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
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
import com.b5.voicecontroll.presenter.presenter.service.AudioService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final int ADD_CODE = 1;  // 新規追加時のrequestCode
    private static final int EDIT_CODE = 2;  // 編集時のrequestCode

    private static MainActivity MainInstance;

    // TODO: 追加予定ダイアログ用
    private static final int FRAGMENT_CODE = 3;  // AlertDialog呼び出し時のrequestCode
    private DialogFragment dialogFragment;
    private FragmentManager fragmentManager;

    private MyAdapter adapter;
    int times[] = {0, 0};

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
                String day = item.getDay();
                clearAlarm(item.getId());
                timeEdit(position, day);
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
                clearAlarm(item.getId());
                return true;
            }
        });
        redisplayList(); // アプリを閉じる前のデータを再表示
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Arrays.fill(times, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveListData(); // 終了時にListViewの中身を保存
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case (ADD_CODE):
                if (resultCode == RESULT_OK) {
                    // timeBox/dayにそれぞれEditActivityから受け取った設定時間の配列/曜日の文字列を格納
                    int timeBox[] = intent.getIntArrayExtra("return_times");
                    String day = intent.getStringExtra("chosen_day");
                    // 配列/文字列の内容をListItemオブジェクトに詰め替え
                    ListItem item = new ListItem();
                    item.setId((new Random()).nextLong());
                    item.setTimes(timeBox);
                    item.setDay(day);
                    adapter.setData(item);
                    sendTimeProcess(adapter.getCount() - 1);
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
                    sendTimeProcess(position); // 編集画面から戻ってくるたびにAlarmをセット
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
        intent.putExtra("select_day", "しない");
        startActivityForResult(intent, ADD_CODE);
    }

    /**
     * ListViewの項目タップで編集画面(EditActivity)に遷移
     *
     * @param position タップした項目の位置
     * @param day      項目の指定曜日
     */
    public void timeEdit(int position, String day) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("edit_times", times);
        intent.putExtra("select_day", day);  // TODO: indexを送ることで省略できそう
        intent.putExtra("list_position", position);
        startActivityForResult(intent, EDIT_CODE);
    }

    /**
     * AlarmManagerに指定時間にTimeReceiveを処理するように登録
     *
     * @param position 設定時間のListViewの要素の位置
     */
    public void sendTimeProcess(int position) {
        final ListView list = findViewById(R.id.list_view);
        ListItem item = (ListItem) list.getItemAtPosition(position);

        Intent intent = new Intent(getApplicationContext(), TimeReceive.class);
        intent.setType(String.valueOf(item.getId()));
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        // 現在の時刻をcalendarにセット
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // ターゲット時刻をcalendar_targetにセット
        Calendar calendar_target = Calendar.getInstance();
        calendar_target.setTimeInMillis(System.currentTimeMillis());
        calendar_target.set(Calendar.HOUR_OF_DAY, item.getTimeBox()[0]);
        calendar_target.set(Calendar.MINUTE, item.getTimeBox()[1]);
        calendar_target.set(Calendar.DAY_OF_WEEK, 1);

        // ターゲットと現在の曜日差を取得
        dayDiff(calendar.get(Calendar.DAY_OF_WEEK), convertDay(item.getDay()));

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // ターゲット時刻が何秒後かを追加
        calendar.add(Calendar.SECOND, returnSecond(calendar, calendar_target, dayDiff(calendar.get(Calendar.DAY_OF_WEEK), convertDay(item.getDay()))));

        // 繰り返しあり・なしで場合分け
        if (item.getDay().equals("しない")) {
            // ターゲット時刻が何秒後かを追加
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000 * 7, sender);
        }
    }

    /**
     * 消去されたListView項目のIDでアラームに登録されている情報の消去
     *
     * @param id 消去したListView項目のID
     */
    public void clearAlarm(long id) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), TimeReceive.class);
        intent.setType(String.valueOf(id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        pendingIntent.cancel();
        am.cancel(pendingIntent);
    }

    /**
     * 設定時刻までの時間情報をSECONDフィールドに変換する
     *
     * @param calendar        現在の時間情報を含んだCalendar型変数
     * @param calendar_target 設定時間の情報を含んだCalendar型変数
     * @param dayDiff         指定時間と現在の曜日の差
     * @return 設定時刻までの秒数
     */
    public int returnSecond(Calendar calendar, Calendar calendar_target, int dayDiff) {
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

        int diffSecond = ((dayDiff * 86400) + (hourDiff * 3600) + (minuteDiff * 60) - calendar.get(Calendar.SECOND) - 2);

        System.out.println(diffSecond);
        return diffSecond;
    }

    /**
     * ListViewに格納されている曜日を文字列からCalendarクラス変数に置換
     *
     * @param day ListViewに格納されている曜日文字列
     * @return 置換後の曜日
     */
    public int convertDay(String day) {
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

    public int dayDiff(int current, int target) {
        int diff = target - current;
        if (diff < 0) diff += 7;
        if (target == 0) diff = 0;
        return diff;
    }

    /**
     * ListViewのデータを保存する
     */
    public void saveListData() {
        SharedPreferences dataSave = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataSave.edit();
        final ListView list = findViewById(R.id.list_view);
        editor.putInt("DataCount", adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            ListItem item = (ListItem) list.getItemAtPosition(i);
            long id = item.getId();
            editor.putLong("SaveID" + i, id);
            editor.putInt("SaveData1" + id, item.getTimeBox()[0]);
            editor.putInt("SaveData2" + id, item.getTimeBox()[1]);
            editor.putString("SaveDay" + id, item.getDay());
        }
        editor.apply();
    }

    /**
     * SharedPreferences型変数で格納しているListViewデータを再表示
     */
    public void redisplayList() {
        SharedPreferences dataSave = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataSave.edit();
        int DataCount = dataSave.getInt("DataCount", 0);
        for (int i = 0; i < DataCount; i++) {
            Long id = dataSave.getLong("SaveID" + i, 0);
            int timeBox[] = {dataSave.getInt("SaveData1" + id, 0), dataSave.getInt("SaveData2" + id, 0)};
            ListItem item = new ListItem();
            item.setId(id);
            item.setTimes(timeBox);
            item.setDay(dataSave.getString("SaveDay" + id, "しない"));
            adapter.setData(item);
        }
        editor.clear();
    }


}
