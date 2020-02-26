package com.bong.fragment.data;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apiservice {
    String API_URL = "https://www.nlotto.co.kr/";

    @GET ("common.do?method=getLottoNumber")
    Call<Lotto>getComment(@Query("drwNo") int drwNo);

    @GET ("common.do?method=getLottoNumber")
    Single<Lotto> getCommentRx(@Query("drwNo") int drwNo);

}
