package com.b5.voicecontroll.presenter.presenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.b5.voicecontroll.R;
import com.b5.voicecontroll.presenter.presenter.adapter.MyAdapter;
import com.b5.voicecontroll.presenter.presenter.entity.ListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final int ADD_CODE = 1;  //新規追加時のrequestCode
    private static final int EDIT_CODE = 2;  //編集時のrequestCode
    private MyAdapter adapter;
    int times[] = {0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                ListItem item = (ListItem) parent.getItemAtPosition(position);
                adapter.deleteData(item);
                return false;
            }
        });
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

}
