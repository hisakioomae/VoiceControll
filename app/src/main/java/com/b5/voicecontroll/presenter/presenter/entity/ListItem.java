package com.b5.voicecontroll.presenter.presenter.entity;

public class ListItem {
    private long id = 0;
    private int times[] = new int[2];
    private String day;

    public long getId() {
        return id;
    }

    public String getTimes() {
        String timeText;
        if (times[1] < 10) {
            // 時と分を文字列として結合
            timeText = times[0] + "時" + "0" + times[1] + "分";
        } else {
            timeText = times[0] + "時" + times[1] + "分";
        }
        if (times[0] < 10) {
            // 表示時のズレ解消のため
            timeText = "  " + times[0] + "時" + "0" + times[1] + "分";
        }
        return timeText;
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