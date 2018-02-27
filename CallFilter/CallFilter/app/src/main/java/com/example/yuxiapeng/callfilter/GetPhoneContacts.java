package com.example.yuxiapeng.callfilter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by yuxiapeng on 2017/11/22.
 */

public class GetPhoneContacts {
    private String getPhoneNumber(String number) {
        String ret = "";
        for (int i = 0; i < number.length(); i++) {
            if (Character.isDigit(number.charAt(i))) {
                ret += number.charAt(i);
            }
        }
        return ret;
    }

    public ArrayList<String> getPhoneContacts(Context context) {
        String[] cols = {
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
        };
        ArrayList<String> ret = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            cursor.getColumnCount();
            for (int j = 0; j < cursor.getColumnCount(); j++) {
                String tmp = cursor.getString(j);
                if (tmp != null && tmp.length() > 0){
                    ret.add(getPhoneNumber(tmp));
                }
            }
//            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
//            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            String name = cursor.getString(nameFieldColumnIndex);
//            String number = cursor.getString(numberFieldColumnIndex);
        }
        return ret;
    }
}
