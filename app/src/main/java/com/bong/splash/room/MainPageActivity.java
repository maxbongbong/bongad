package com.bong.splash.room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.lottoapi.Apiservice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start_button = findViewById(R.id.join_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
            }
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



