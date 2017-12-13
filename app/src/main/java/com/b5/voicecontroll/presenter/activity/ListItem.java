package com.b5.voicecontroll.presenter.activity;

public class ListItem {
    private long id = 0;
    private int times[] = new int[4];

    public long getId(){ return id; }
    public String getTimes(){
        return String.format("%d時%d分～%d時%d分", times[0], times[1], times[2], times[3]); }

    public void setId(long id){ this.id = id; }
    public void setTimes(int times[]){ this.times = times; }
}
