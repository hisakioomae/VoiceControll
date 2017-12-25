package com.b5.voicecontroll.presenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.b5.voicecontroll.R;
import com.b5.voicecontroll.presenter.activity.adapter.MyAdapter;
import com.b5.voicecontroll.presenter.entity.ListItem;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<ListItem> data = new ArrayList<>();
        adapter = new MyAdapter(this, data);
        final ListView list = findViewById(R.id.list_view);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                ListItem item = (ListItem) listView.getItemAtPosition(position);
                adapter.deleteData(item);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    //timeBox/dayにそれぞれEditActivityから受け取った設定時間の配列/曜日の文字列を格納
                    int timeBox[] = intent.getIntArrayExtra("return_times");
                    String day = intent.getStringExtra("chosen_day");
                    // 配列/文字列の内容をListItemオブジェクトに詰め替え
                    ArrayList<ListItem> data = new ArrayList<>();
                    ListItem item = new ListItem();
                    item.setId((new Random()).nextLong());
                    item.setTimes(timeBox);
                    item.setDay(day);
                    data.add(item);
                    adapter.setData(item);
                    break;
                }
        }
    }

    /**
     * 編集ボタンタップでEditActivityに遷移
     *
     * @param view activity_main.xml
     */
    public void timeEdit(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

}
