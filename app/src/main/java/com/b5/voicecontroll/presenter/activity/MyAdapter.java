package com.b5.voicecontroll.presenter.activity;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.b5.voicecontroll.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ListItem> data = new ArrayList<>();
    private int resource = 0;

    public MyAdapter(Context context, ArrayList<ListItem> data, int resource) {
        this.context = context;
        this.data = data;
        this.resource = resource;
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
        ((TextView)convertView.findViewById(R.id.setting_times)).setText(String.valueOf(item.getTimes()));

        return convertView;
    }

    public void setData(ListItem data){
        this.data.add(data);
        notifyDataSetChanged();
    }
}

