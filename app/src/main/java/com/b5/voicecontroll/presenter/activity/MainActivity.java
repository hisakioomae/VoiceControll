package com.b5.voicecontroll.presenter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.b5.voicecontroll.R;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ListItem> data = new ArrayList<>();
        adapter = new MyAdapter(this, data);
        ListView list = findViewById(R.id.list_view);
        list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    //timeBoxにEditActivityから受け取った配列を格納
                    int timeBox[] = intent.getIntArrayExtra("return_times");
                    // 配列の内容をListItemオブジェクトに詰め替え
                    ArrayList<ListItem> data = new ArrayList<>();
                    ListItem item = new ListItem();
                    item.setId((new Random()).nextLong());
                    item.setTimes(timeBox);
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
