package com.b5.voicecontroll.presenter.entity;

public class ListItem {
    private long id = 0;
    private int times[] = new int[4];
    private String day;

    public long getId() {
        return id;
    }

    public String getTimes() {
        return times[0] + "時" + times[1] + "分" + "～" + times[2] + "時" + times[3] + "分";
    }

    public String getDay() {
        return day;
    }

    public int[] getTimeBox() {
        return times;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimes(int times[]) {
        this.times = times;
    }

    public void setDay(String day) {
        this.day = day;
    }
}