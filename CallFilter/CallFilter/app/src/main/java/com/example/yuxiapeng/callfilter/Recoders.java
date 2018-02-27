package com.example.yuxiapeng.callfilter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by yuxiapeng on 2017/11/27.
 */

public class Recoders {
    private ArrayList<RecoderItem> recoders;
    private SQLiteDatabase db;
    private String DB_PATH = "/data/data/com.example.yuxiapeng.callfilter/databases";
    private String DB_NAME = "recoderDB";
    private String RECODERS_TBL_NAME = "recoders";

    Recoders() {
        this.recoders = new ArrayList<>();
        initDB();
    }

    public void initDB() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
//        String sql = "DROP TABLE " + this.RECODERS_TBL_NAME;
//
//        db.execSQL(sql);
        String stu_table = "create table if not exists " + this.RECODERS_TBL_NAME + "(" +
                "_id integer primary key autoincrement, " +
                "phone_number text, " +
                "times integer," +
                "time_stamp integer)";
        if (!this.checkTblExist(this.RECODERS_TBL_NAME)) {
            db.execSQL(stu_table);
        }

    }

    private boolean checkTblExist(String tabName) {
        boolean ret = false;
        String sql = "select name from sqlite_master where type='table';";
        Cursor cursor;
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            if (name.equals(tabName)) {
                ret = true;
            }
        }
        cursor.close();
        return ret;
    }

    private boolean checkPhoneNumberExist(String phoneNumber) {
        boolean ret = false;
        String sql = "select phone_number from " + this.RECODERS_TBL_NAME + " where phone_number='" + phoneNumber + "';";
        Cursor cursor;
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(0);
            if (tmp.equals(phoneNumber)) {
                ret = true;
            }
        }
        cursor.close();
        return ret;
    }

    public void incrementTimes(String phoneNumber) {
        long ts = System.currentTimeMillis();
        if (checkPhoneNumberExist(phoneNumber)) {
            RecoderItem item = this.getItem(phoneNumber);
            ContentValues values = new ContentValues();
            values.put("times", item.getTimes() + 1);
            values.put("time_stamp", ts);
            String whereClause = "phone_number=?";
            String[] whereArgs = {phoneNumber};
            db.update(this.RECODERS_TBL_NAME, values, whereClause, whereArgs);
        } else {
            ContentValues cValue = new ContentValues();
            cValue.put("phone_number", phoneNumber);
            cValue.put("times", 1);
            cValue.put("time_stamp", ts);
            db.insert(this.RECODERS_TBL_NAME, null, cValue);
        }
    }

    public void deleteRecoder(int index) {
        RecoderItem item = this.getItem(index);
        String whereClause = "phone_number=?";
        String[] whereArgs = {item.getPhoneNumber()};
        db.delete(this.RECODERS_TBL_NAME, whereClause, whereArgs);
    }

    public int count() {
        Cursor cursor;
        int ret = 0;
        String sql = "select count(*) from " + this.RECODERS_TBL_NAME + ";";
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            ret = cursor.getInt(0);
        }
        cursor.close();
        return ret;

    }

    public RecoderItem getItem(int index) {
        this.recoders.clear();
        Cursor cursor;
        String sql = "select * from " + this.RECODERS_TBL_NAME + ";";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            RecoderItem item = new RecoderItem();
            item.setPhoneNumber(cursor.getString(1));
            item.setTimes(cursor.getInt(2));
            item.setTimeStamp(cursor.getLong(3));
            this.recoders.add(item);
        }
        cursor.close();
        Collections.sort(this.recoders);
        return this.recoders.get(index);
    }

    public RecoderItem getItem(String phoneNumber) {
        RecoderItem item = new RecoderItem();

        String sql = "select * from " + this.RECODERS_TBL_NAME + " where phone_number=" + phoneNumber + ";";
        Cursor cursor;
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            item.setPhoneNumber(cursor.getString(1));
            item.setTimes(cursor.getInt(2));
            item.setTimeStamp(cursor.getLong(3));
        } else {
            item.setPhoneNumber("null");
            item.setTimes(0);
            item.setTimeStamp(0);
        }
        cursor.close();
        return item;

    }
}
