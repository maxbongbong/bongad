package com.bong.splash;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Apiservice {
    public static final String API_URL = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&amp;drwNo=";

    @GET ("comments")
    Call<ResponseBody>getComment(@Query("postId") int postId);

    @POST ("comments")
    Call<ResponseBody>getPostComment(@Query("postId") int postId);


    // String version

    @GET ("comments")
    Call<ResponseBody>getCommentStr(@Query("postId") String postId);

    @FormUrlEncoded
    @POST ("comments")
    Call<ResponseBody>getPostCommentStr(@Field("postId") String postId);
}
