package com.bong.splash.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.data.Apiservice;
import com.bong.splash.network.RetrofitMaker;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;
import com.bong.splash.ui.history.HistoryActivity;
import com.bong.splash.ui.trend.TrendActivity;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {

    TextView v_result = findViewById(R.id.v_result);
    protected CompositeDisposable disposables;
    LottoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button join_button = findViewById(R.id.bt_generate);
        join_button.setOnClickListener(v -> {
            View view = findViewById(R.id.v_result);
            view.setVisibility(View.VISIBLE);


        });

        Button history_button = findViewById(R.id.bt_history);
        history_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });

        Button trend_button = findViewById(R.id.bt_trend);
        trend_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TrendActivity.class);
            startActivity(intent);
        });

        callAPIs();
    }

    void getLotto() {

        disposables.add(AppDatabase.getDatabase(this).getLottoDao().findLotto(1)     //getLotto(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {

                    @Override
                    public void onSuccess(Lotto lotto) {
                        Log.e("Lotto", "로또 뿌리기 : " + lotto.drwNo);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                }));
    }

    //디비에서 가져오기
    Single<Lotto> getLotto(int lottoNo) {
        //여기에 로또 저장하기 넣기
        //return Single.just(new Lotto());
        return dao.findLotto(lottoNo);
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



