package com.bong.fragment.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Frequency {
    @PrimaryKey
    public int drwNo; //회차
    public int drwtNo1;
    public int drwtNo2;
    public int drwtNo3;
    public int drwtNo4;
    public int drwtNo5;
    public int drwtNo6;
    public int bnusNo; //보너스번호
}
