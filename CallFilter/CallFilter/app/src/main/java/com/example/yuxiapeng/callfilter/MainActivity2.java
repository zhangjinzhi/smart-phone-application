package com.example.yuxiapeng.callfilter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yuxiapeng on 2017/11/30.
 */

public class MainActivity2 extends AppCompatActivity {
    private ListView listView;
    private Handler handler = new Handler();
    private ArrayList<String> data;
    private ArrayList<String> checked;
    private BlackList blackList = new BlackList();
    private BlackListAdapter adpter;
    private Context context;
    private TextView note;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        this.setTitle("Check Black List");
        context = this;
        listView = (ListView) this.findViewById(R.id.blacklistview);
        note = (TextView) findViewById(R.id.note);
        data = blackList.getBlackList();
        checked = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            checked.add("NO");
        }
        adpter = new BlackListAdapter(data, checked, this);
        listView.setAdapter(adpter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (checked.get(arg2).equals("YES")) {
                    checked.set(arg2, "NO");
                } else {
                    checked.set(arg2, "YES");
                }
//                BlackListAdapter.ViewHolder viewHolder = (BlackListAdapter.ViewHolder) arg1.getTag();
//                viewHolder.checkBox.toggle();
//                if (viewHolder.checkBox.isChecked()) {
//                    viewHolder.checkBox.setChecked(false);
//                    Toast.makeText(context, String.valueOf(arg3), Toast.LENGTH_SHORT).show();
//                } else {
//                    viewHolder.checkBox.setChecked(true);
//                    Toast.makeText(context, String.valueOf(arg3), Toast.LENGTH_SHORT).show();
//
//                }
            }
        });

        this.handler.postDelayed(runnable, 500);
    }

    public void selectAll(View v) {
        for (int i = 0; i < checked.size(); i++) {
            checked.set(i, "YES");
        }
    }

    public void clearSelect(View v) {
        for (int i = 0; i < checked.size(); i++) {
            checked.set(i, "NO");
        }
    }

    public void deleteSelect(View v) {
        int size = checked.size();
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < checked.size(); i++) {
                if (checked.get(i).equals("YES")) {
                    blackList.delete(i);
                    checked.remove(i);
                    break;
                }
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            data = blackList.getBlackList();
            if (data.size() == 0) {
                note.setText("There is no black phone number!");
            }
            else note.setText(" ");
            adpter.notifyDataSetChanged();
            handler.postDelayed(this, 500);
        }
    };
}
