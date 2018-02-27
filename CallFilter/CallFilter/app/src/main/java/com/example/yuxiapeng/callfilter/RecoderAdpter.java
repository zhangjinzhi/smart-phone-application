package com.example.yuxiapeng.callfilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by yuxiapeng on 2017/12/2.
 */

public class RecoderAdpter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Recoders recoders;

    RecoderAdpter(Context context, Recoders recoders) {
        this.recoders = recoders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.recoders.count();
    }

    @Override
    public Object getItem(int position) {
        return this.recoders.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.record_list_entry, parent, false); //加载布局
            holder = new ViewHolder();

            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
            holder.times = (TextView) convertView.findViewById(R.id.times);

            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RecoderItem it = recoders.getItem(position);
        holder.phoneNumber.setText(it.getPhoneNumber());
        holder.date.setText(format.format(it.getTimeStamp()));
        holder.times.setText(String.valueOf(it.getTimes()) + " times");
        return convertView;
    }

    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
    private class ViewHolder {
        TextView date;
        TextView phoneNumber;
        TextView times;
    }

}

