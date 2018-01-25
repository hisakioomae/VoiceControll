package com.b5.voicecontroll.presenter.presenter.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.b5.voicecontroll.presenter.presenter.fragment.AlertDialogFragment;  // TODO: Dialog追加予定

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
        sendTimeProcess();  // ListViewに格納されている時間で処理を行うように設定
    }




    @Override
    public void onRestart() {
        super.onRestart();
        Arrays.fill(times, 0);

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
     * TimeReceiveクラスのインテントを作成しAlarmManagerに設定時刻を登録
     */
    public void sendTimeProcess(){
        Intent intent = new Intent(getApplicationContext(), TimeReceive.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis()); // 現在時刻を取得
        System.out.println(calendar.HOUR_OF_DAY);
        calendar.add(Calendar.SECOND, 5); // 現時刻より2秒後を設定

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

}
