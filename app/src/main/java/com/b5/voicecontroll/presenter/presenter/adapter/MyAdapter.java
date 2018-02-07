package com.b5.voicecontroll.presenter.presenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.b5.voicecontroll.R;
import com.b5.voicecontroll.presenter.presenter.activity.MainActivity;
import com.b5.voicecontroll.presenter.presenter.entity.ListItem;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> listData = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<ListItem> data) {
        this.context = context;
        this.listData = data;
    }

    static class ViewHolder {
        TextView timeText;
        TextView dayText;
        Switch swt;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listData.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        Activity activity = (Activity) context;
        ListItem item = (ListItem) getItem(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater()
                    .inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.timeText = convertView.findViewById(R.id.setting_times);
            holder.dayText = convertView.findViewById(R.id.setting_day);
            holder.swt = convertView.findViewById(R.id.list_switch);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.swt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, R.id.list_switch);
            }
        });
        
        ((TextView) convertView.findViewById(R.id.setting_times)).setText(String.valueOf(item.getTimes()));
        ((TextView) convertView.findViewById(R.id.setting_day)).setText(item.getDay());
        return convertView;
    }

    /**
     * 設定時間保存時にListViewの更新
     *
     * @param addData ListItem型の新たに追加した設定時間
     */
    public void setData(ListItem addData) {
        this.listData.add(addData);
        notifyDataSetChanged();
    }

    /**
     * ListView項目の長押しで消去
     *
     * @param item 選択した項目
     */
    public void deleteData(ListItem item) {
        this.listData.remove(item);
        notifyDataSetChanged();
    }

    /**
     * 選択元の項目を編集した項目に置換
     *
     * @param position 選択した項目のListView内の位置
     * @param editData 編集した項目
     */
    public void changeData(int position, ListItem editData) {
        this.listData.set(position, editData);
        notifyDataSetChanged();
    }
}

