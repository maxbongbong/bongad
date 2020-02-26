package com.bong.fragment.ui.Trend;

class LottoNum {

    String LottoNum;
    String WinNum;

    public String getNum() {
        return LottoNum;
    }

    public void setNum(String num) {
        LottoNum = num;
    }

    public String getWin() {
        return WinNum;
    }

    public void setWin(String win) {
        WinNum = win;
    }

    public LottoNum(String Num, String Win){
        this.LottoNum = Num;
        this.WinNum = Win;
    }
}
