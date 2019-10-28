package com.bong.splash.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;
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
//    TextView tv = new TextView(this);
//    EditText tv_event_number = (EditText)findViewById(R.id.tv_event_number);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(this).getLottoDao();

        Button join_button = findViewById(R.id.bt_generate);
        join_button.setOnClickListener(v -> {
            View view = findViewById(R.id.v_result);
            view.setVisibility(View.VISIBLE);

        });

        Button result_bt = findViewById(R.id.bt_match);
        result_bt.setOnClickListener(v -> {
//            tv.setText();
//            String 당첨번호 = generateLotto();
//            tv.setText(당첨번호);
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
                if (text.length() != 0 ){
                    join_button.setEnabled(true); //만약에 작성중일땐 버튼이보이도록하고
                }else{
                    join_button.setEnabled(false); //글자가 아무것도없다면 버튼이 안보이도록 합니다.
                }
            }


            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // 입력하기 전에

            }

        });


    }

    void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }



//    String generateLotto(){
//
//    }

    void getLotto() {


        disposables.add(AppDatabase.getDatabase(this).getLottoDao().findLotto(3)
                //getLotto(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {

                    @Override
                    public void onSuccess(Lotto lotto) {

                        Log.e("LottoDBJoin", "DbJoin" + lotto.drwNo + "," + lotto.drwtNo1 + "," + lotto.drwtNo2 + "," + lotto.drwtNo3 + "," + lotto.drwtNo4 + "," + lotto.drwtNo5 + "," + lotto.drwtNo6 + "," + lotto.drwNoDate);

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



