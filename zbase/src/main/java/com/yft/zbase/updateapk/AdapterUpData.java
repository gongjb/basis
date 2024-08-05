package com.yft.zbase.updateapk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yft.zbase.R;

import java.util.ArrayList;


/**
 * 展示更新内容的适配器
 */

public class AdapterUpData extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public AdapterUpData(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_updata, null);
            holder.tv = view.findViewById(R.id.tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv.setText(list.get(i));
        return view;
    }

    class ViewHolder {
        // 展示更新内容的textview
        private TextView tv;
    }
}
