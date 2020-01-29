package com.bong.splash.ui.history;

public class HistoryViewModel {
    private int DrwNo; //회차번호

    private int Winnum;//추첨번호

    public HistoryViewModel(int drwNo, int winnum) {
        DrwNo = drwNo;
        Winnum = winnum;
    }

    public int getDrwNo() {
        return DrwNo;
    }

    public void setDrwNo(int drwNo) {
        DrwNo = drwNo;
    }

    public int getWinnum() {
        return Winnum;
    }

    public void setWinnum(int winnum) {
        Winnum = winnum;
    }

    @Override
    public String toString() {
        return "HistoryViewModel{" +
                "DrwNo=" + DrwNo +
                ", Winnum=" + Winnum +
                '}';
    }
}
