package com.example.yuxiapeng.callfilter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuxiapeng on 2017/11/30.
 */

public class BlackList {
    ArrayList<String> blackList;
    private SQLiteDatabase db;
    private String DB_PATH = "/data/data/com.example.yuxiapeng.callfilter/databases";
    private String DB_NAME = "recoderDB";
    private String TABLE = "black_list";

    private void initDB() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
        String stu_table = "create table if not exists " + this.TABLE + "(" +
                "_id integer primary key autoincrement, " +
                "phone_number text)";
        db.execSQL(stu_table);
    }

    public ArrayList<String> getBlackList() {
        this.readBlackListFromDB();
        return blackList;
    }

    BlackList() {
        this.blackList = new ArrayList<>();
        initDB();
    }

    public int count(){
        return this.blackList.size();
    }

    public boolean contain(String phoneNumber) {
        this.readBlackListFromDB();
        if (this.blackList.contains(phoneNumber)) {
            return true;
        } else {
            return false;
        }
    }

    private void readBlackListFromDB() {
        this.blackList.clear();
        String sql = "select phone_number from " + this.TABLE + ";";
        Cursor cursor;
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            this.blackList.add(cursor.getString(0));
        }
        cursor.close();
    }

    public void delete(String phoneNumber) {
        String whereClause = "phone_number=?";
        String[] whereArgs = {phoneNumber};
        db.delete(this.TABLE, whereClause, whereArgs);
    }

    public void delete(int index) {
        this.readBlackListFromDB();
        this.delete(this.blackList.get(index));
    }

    public void add(String phoneNumber) {
        ContentValues cValue = new ContentValues();
        cValue.put("phone_number", phoneNumber);
        db.insert(this.TABLE, null, cValue);
    }

}
