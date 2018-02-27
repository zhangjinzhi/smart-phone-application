package com.example.yuxiapeng.callfilter;

/**
 * Created by yuxiapeng on 2017/12/2.
 */


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class BlackListAdapter extends BaseAdapter {
    List<String> list;
    List<String> checked;
    Context context;

    public BlackListAdapter(List<String> list, List<String> checked, Context context) {
        this.list = list;
        this.context = context;
        this.checked = checked;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.black_list_entry, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position));
        if (checked.get(position).equals("YES")){
            viewHolder.checkBox.setChecked(true);
        }
        else{
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }
}
