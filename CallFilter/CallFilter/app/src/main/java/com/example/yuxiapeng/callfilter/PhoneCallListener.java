package com.example.yuxiapeng.callfilter;

/**
 * Created by yuxiapeng on 2017/11/22.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PhoneCallListener extends PhoneStateListener {
    private Context context;
    private GetPhoneContacts getPhoneContacts;
    private ArrayList<HashMap<String, Object>> listViewData;
    private Recoders recoders;
    private BlackList blackList;
    private AlertDialog dialog;

    public void setStarted(boolean started) {
        this.started = started;
    }

    private boolean started;

    PhoneCallListener(Context _context, Recoders _recoders, BlackList blackList, boolean started) {
        this.context = _context;
        this.getPhoneContacts = new GetPhoneContacts();
        this.recoders = _recoders;
        this.blackList = blackList;
        this.started = started;

    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                System.out.println("call state is idle:" + incomingNumber);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                System.out.println("call state is offhook:" + incomingNumber);
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                if (this.started) {
                    callInHand(incomingNumber);
                }
                break;
        }
        super.onCallStateChanged(state, incomingNumber);
    }

    private void callInHand(String incomingNumber) {
        ArrayList<String> contacts = getPhoneContacts.getPhoneContacts(context);
        System.out.println("call state is ringing:" + incomingNumber);
        System.out.println("call state is ringing:" + contacts.toString());
        if (!contacts.contains(incomingNumber)) {
            System.out.println("not in contacts");
            if (this.blackList.contain(incomingNumber)) {
                recoders.incrementTimes(incomingNumber);
                this.endCall();
                return;
            }
            showDialog(incomingNumber);
        } else {
            System.out.println("in contacts");
        }
    }

    private void showDialog(final String phoneNumber) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.alter_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        builder.setTitle("Warning");
        TextView tv = view.findViewById(R.id.alter_dialog_message);
        tv.setText(phoneNumber + " is a strange call" + "\nAdd to blacklist?");
        TextView ensure = view.findViewById(R.id.alter_dialog_button_ensure);
        ensure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "add: " + phoneNumber, Toast.LENGTH_SHORT).show();
                recoders.incrementTimes(phoneNumber);
                blackList.add(phoneNumber);
                dialog.dismiss();
                endCall();

            }
        });
        TextView cancel = view.findViewById(R.id.alter_dialog_button_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(context, "ignore: " + phoneNumber, Toast.LENGTH_SHORT).show();
            }
        });

//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(context, "ignore: " + phoneNumber, Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(context, "add: " + phoneNumber, Toast.LENGTH_SHORT).show();
//                recoders.incrementTimes(phoneNumber);
//                blackList.add(phoneNumber);
//                endCall();
//            }
//        });
//        builder.setMessage(phoneNumber +" is a strange call"+ "\nAdd to blacklist?");
//      dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);

        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }


    private void endCall() {
        try {
            // 延迟5秒后自动挂断电话
            // 首先拿到TelephonyManager
            TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Class<TelephonyManager> c = TelephonyManager.class;

            // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象

            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);

            //允许访问私有方法
            mthEndCall.setAccessible(true);
            final Object obj = mthEndCall.invoke(telMag, (Object[]) null);

            // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
            Method mt = obj.getClass().getMethod("endCall");
            //允许访问私有方法
            mt.setAccessible(true);
            mt.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}