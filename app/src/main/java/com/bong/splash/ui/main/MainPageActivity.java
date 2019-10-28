package com.bong.splash.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.data.Apiservice;
import com.bong.splash.network.RetrofitMaker;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;
import com.bong.splash.ui.history.HistoryActivity;
import com.bong.splash.ui.trend.TrendActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    LottoDao dao;
    TextView tv_generate = new TextView(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(this).getLottoDao();


        //생성하기 버튼
        Button join_button = findViewById(R.id.bt_generate);
        join_button.setOnClickListener(v -> {
            View view = findViewById(R.id.v_result);
            view.setVisibility(View.VISIBLE);
            showToast();
            tv_generate.findViewById(R.id.tv_lotto);

        });


        //결과보기 버튼
        Button result_bt = findViewById(R.id.bt_match);
        result_bt.setOnClickListener(v -> {
//            generate_num = generate_number();
//            .setText(generate_num);
            callAPIs();
        });


        //히스토리 버튼
        Button history_button = findViewById(R.id.bt_history);
        history_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });


        //트렌드 버튼
        Button trend_button = findViewById(R.id.bt_trend);
        trend_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), TrendActivity.class);
            startActivity(intent);
        });


        //생성하기 버튼 리스너 속성 추가
        TextView editText = findViewById(R.id.tv_event_number);
        editText.addTextChangedListener(new TextWatcher() {

            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // 입력되는 텍스트에 변화가 있을 때
            }


            @Override

            public void afterTextChanged(Editable s) {

                // 입력이 끝났을 때
                String text = s.toString();
                if (text.length() != 0) {
                    join_button.setEnabled(true); //버튼 활성화
                } else {
                    join_button.setEnabled(false); //버튼 비활성화
                }
            }


            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // 입력하기 전에

            }

        });


    }

    void showToast(){
         Toast.makeText(this.getApplicationContext(), R.string.generate_num, Toast.LENGTH_LONG).show();
    }


    //로또 번호 생성
    Object generate_number(){

        List<Integer> result = new ArrayList<Integer>();
        List<Integer> list = new ArrayList<Integer>();

        // List 안에 로또번호 추가
        for (int i = 1; i <= 45; i++) {
            list.add(i);
        }

        for (int i = 0; i < 6; i++) {
            final int idx = new Random().nextInt(list.size());
            result.set(i, list.get(i));
            result.add(list.get(idx));
            list.remove(idx);
        }

        // 정렬
        Arrays.sort(new List[]{list});

        final int idx = new Random().nextInt(list.size());

        result.add(list.get(idx));
        list.remove(idx);

        final String s = Arrays.toString(new List[]{list});
        return s;

    }


    //Single패턴 DB조회하기
    void Winning_number(){

         disposables.add(AppDatabase.getDatabase(this).getLottoDao().findLotto(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {
                    @Override
                    public void onSuccess(Lotto lotto) {
                        Log.e("Lottonum", "number" + lotto.drwNo + "," + lotto.bnusNo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

    void getLotto() {

        disposables.add(AppDatabase.getDatabase(this).getLottoDao().findLotto(3)
                //getLotto(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {

                    @Override
                    public void onSuccess(Lotto lotto) {

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

    Single<List<Lotto>> getLottos(int lottoNo) {
        //여기에 로또 저장하기 넣기
        ArrayList<Lotto> list = new ArrayList<>();
        list.add(new Lotto());
        return Single.just(list);
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



