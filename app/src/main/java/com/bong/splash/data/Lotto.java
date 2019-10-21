package com.bong.splash.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Lotto {
    @PrimaryKey
    public float drwNo; //회차
    public float drwtNo1;
    public float drwtNo2;
    public float drwtNo3;
    public float drwtNo4;
    public float drwtNo5;
    public float drwtNo6;
    public float bnusNo; //보너스번호
    public String drwNoDate; //날짜
    public float totSellamnt;
    public String returnValue;
    public float firstWinamnt;
    public float firstPrzwnerCo;
    public float firstAccumamnt;

}