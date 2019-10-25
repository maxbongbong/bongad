package com.bong.splash.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Lotto {
    @PrimaryKey
    public int drwNo; //회차
    public int drwtNo1;
    public int drwtNo2;
    public int drwtNo3;
    public int drwtNo4;
    public int drwtNo5;
    public int drwtNo6;
    public int bnusNo; //보너스번호
    public String drwNoDate; //날짜
    public String totSellamnt;
    public String returnValue;
    public String firstWinamnt;
    public String firstPrzwnerCo;
    public String firstAccumamnt;
}