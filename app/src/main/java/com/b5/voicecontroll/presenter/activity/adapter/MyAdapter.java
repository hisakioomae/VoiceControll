package com.b5.voicecontroll.presenter.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.b5.voicecontroll.R;
import com.b5.voicecontroll.presenter.entity.ListItem;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> data = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<ListItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) context;
        ListItem item = (ListItem) getItem(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater()
                    .inflate(R.layout.list_item, null);
        }
        ((TextView) convertView.findViewById(R.id.setting_times)).setText(String.valueOf(item.getTimes()));
        ((TextView) convertView.findViewById(R.id.setting_day)).setText(item.getDay());
        return convertView;
    }

    /**
     * 設定時間保存時にListViewの更新
     *
     * @param data ListItem型の新たに追加した設定時間
     */
    public void setData(ListItem data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    /**
     * ListView項目の長押しで消去
     *
     * @param item 選択した項目
     */
    public void deleteData(ListItem item) {
        this.data.remove(item);
        notifyDataSetChanged();
    }
}

