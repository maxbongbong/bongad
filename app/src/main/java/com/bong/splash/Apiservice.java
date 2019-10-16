package com.bong.splash;

import com.bong.splash.data.Lotto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Apiservice {
    public static final String API_URL = "https://www.dhlottery.co.kr/";

    @GET ("common.do?method=getLottoNumber")
    Call<Lotto>getComment(@Query("drwNo") int drwNo);
}
