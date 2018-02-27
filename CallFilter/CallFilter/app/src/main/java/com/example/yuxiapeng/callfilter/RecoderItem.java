package com.example.yuxiapeng.callfilter;

/**
 * Created by yuxiapeng on 2017/11/23.
 */

public class RecoderItem implements Comparable<RecoderItem> {
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public RecoderItem() {

    }

    @Override
    public int compareTo(RecoderItem o) {
        return (int) (o.timeStamp - this.timeStamp);
    }

    private long timeStamp;
    private String phoneNumber;
    private int times;
}
