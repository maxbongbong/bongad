package com.bong.splash;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    String URL = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo="; // 서버 API

    retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit server = retrofit.create(Retrofit.class);
}
