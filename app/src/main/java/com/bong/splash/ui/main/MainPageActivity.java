package com.bong.splash.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.data.Apiservice;
import com.bong.splash.network.RetrofitMaker;
import com.bong.splash.ui.history.HistoryActivity;
import com.bong.splash.ui.trend.TrendActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button join_button = findViewById(R.id.bt_generate);
//        join_button.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), TrendActivity.class);
//            startActivity(intent);
//        });

        Button history_button = findViewById(R.id.bt_match);
        history_button.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent1);
        });

        Button trend_button = findViewById(R.id.bt_trend);
        trend_button.setOnClickListener(v -> {
            Intent intent2 = new Intent(getApplicationContext(), TrendActivity.class);
            startActivity(intent2);
        });

        callAPIs();
    }

    void callAPIs(){
        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);

        Call<Lotto> commentStr = apiService.getComment(2);
        commentStr.enqueue(new Callback<Lotto>() {
            @Override
            public void onResponse(Call<Lotto> call, Response<Lotto> response) {
                boolean isSuccessful = response.isSuccessful();
                Log.v("Test3", "isSuccessful:" + isSuccessful);
                if(isSuccessful){
                    Lotto lotto  =response.body();
                    Log.v("Test3", "drwNo:" + lotto.drwNo);
                }
            }

            @Override
            public void onFailure(Call<Lotto> call, Throwable t) {

            }
        });


    }


}



