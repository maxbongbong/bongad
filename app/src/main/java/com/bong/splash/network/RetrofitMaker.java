package com.bong.splash.network;

import android.content.Context;
import android.util.Log;

import com.bong.splash.BuildConfig;
import com.bong.splash.data.Apiservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//SingleTon Design Pattern
public class RetrofitMaker {
    private static Retrofit retrofit;
    private OkHttpClient okHttpClient;
    Dispatcher dispatcher;

    private static final int timeout_read = 20;
    private static final int timeout_connect = 20;
    private static final int timeout_write = 30;

    private static Retrofit getRetrofit() {
        if (retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(Apiservice.API_URL).build();
        return retrofit;
    }

    private static Gson getGson() {
        return new GsonBuilder()
                //.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .create();
    }
    private Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Apiservice.API_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(getGson()));

    public <S> S createService(final Context context, Class<S> serviceClass) {
        Retrofit retrofit = builder.client(getHttpClient(context.getApplicationContext())).build();
        return retrofit.create(serviceClass);
    }

    private OkHttpClient getHttpClient(final Context context) {
        if (okHttpClient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(timeout_connect, TimeUnit.SECONDS);
            httpClientBuilder.readTimeout(timeout_read, TimeUnit.SECONDS);
            httpClientBuilder.writeTimeout(timeout_write, TimeUnit.SECONDS);
            //httpClientBuilder.protocols(getProtocols());

//            if (true) {
            if (BuildConfig.DEBUG) {
                //httpClientBuilder.addInterceptor(new MockInterceptor(context));
                //httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (isJSONValid(message))
                            Logger.json(message);
                        else
                            Log.d("PRETTYLOGGER", message);
                    }

                    public boolean isJSONValid(String jsonInString) {
                        try {
                            new JSONObject(jsonInString);
                        } catch (JSONException ex) {
                            // edited, to include @Arthur's comment
                            // e.g. in case JSONArray is valid as well...
                            try {
                                new JSONArray(jsonInString);
                            } catch (JSONException ex1) {
                                return false;
                            }
                        }
                        return true;
                    }

                });
                logging.level(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addInterceptor(logging);

            }
            //httpClientBuilder.connectionPool(new ConnectionPool(1, 5, TimeUnit.MINUTES));

            dispatcher = new Dispatcher();
            httpClientBuilder.dispatcher(dispatcher);
            //httpClientBuilder.authenticator(new TokenAuthenticator(context));

//            httpClientBuilder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    return convertInterceptToResponse(context, chain);
//                }
//            });
            okHttpClient = httpClientBuilder.build();
        }
        return okHttpClient;
    }
}


