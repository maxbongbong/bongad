package com.bong.splash.ui.main;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bong.splash.R;
import com.bong.splash.data.Apiservice;
import com.bong.splash.data.Lotto;
import com.bong.splash.network.RetrofitMaker;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;
import com.bong.splash.ui.history.HistoryActivity;
import com.bong.splash.ui.trend.TrendActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//- 1등은 번호 6개를 모두 맞춰야함
//        - 2등은 번호 5개 + 보너스 번호 1개를 맞춰야함
//        - 3등은 번호 5개를 맞춰야함
//        - 4등은 번호 4개를 맞춰야함
//        - 5등은 번호 3개를 맞춰야함


public class MainPageActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    LottoDao dao;
    TextView tv_generate;
    EditText tv_result;
    List<Integer> Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(this).getLottoDao();

        tv_generate = findViewById(R.id.tv_lotto);


        Intent intent = getIntent();

        ArrayList<String> list = intent.getStringArrayListExtra("list");
        ArrayList<String> str = intent.getStringArrayListExtra("str");
        ArrayList<Integer>pre = intent.getIntegerArrayListExtra("pre");

        //생성하기 버튼
        Button generate_button = findViewById(R.id.bt_generate);
        generate_button.setOnClickListener(v -> {
            View view = findViewById(R.id.v_result);
            view.setVisibility(View.VISIBLE);
            showToast();
            getLottoTicket();
            tv_generate.setText(convertIntoString(Result));

            //EditText값 가져오기
            tv_result = findViewById(R.id.tv_event_number);
            tv_result.getText().toString();

        });

        //결과보기 버튼
        Button result_bt = findViewById(R.id.bt_match);
        result_bt.setOnClickListener(v -> {
            callAPIs();
        });

        //히스토리 버튼
        Button history_button = findViewById(R.id.bt_history);
        history_button.setOnClickListener(v -> {

            Intent newIntent = new Intent(getApplicationContext(), HistoryActivity.class);
            newIntent.putStringArrayListExtra("list", list);
            newIntent.putStringArrayListExtra("str", str);
            startActivity(newIntent);
        });

        //트렌드 버튼
        Button trend_button = findViewById(R.id.bt_trend);
        trend_button.setOnClickListener(v -> {
            Intent newIntent = new Intent(getApplicationContext(), TrendActivity.class);
            newIntent.putIntegerArrayListExtra("pre", pre);
            Log.e("pre", "pre1 = " + pre);
            startActivity(newIntent);
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
                    generate_button.setEnabled(true); //버튼 활성화
                } else {
                    generate_button.setEnabled(false); //버튼 비활성화
                }
            }

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });
    }


    //토스트 메시지 띄우기
    void showToast() {
        Toast.makeText(this.getApplicationContext(), R.string.generate_num, Toast.LENGTH_LONG).show();
    }

    //결과보기-팝업창
    void show(List<Integer> Win){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("결과보기");

        String str = convertIntoString(Result);
        String str1 = convertIntoString(Win);
        String drwNo = tv_result.getText().toString();
        builder.setMessage("나의 번호 = [" + str + "]\n" + drwNo + "회번호 = [" + str1 + "]\n" + LottoRank(Win));
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"확인", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    //로또 등수확인
    public String LottoRank(List<Integer> Win) {
        int cnt = 0;
        int bns = 0;
        String str;
//        25회 당첨 번호 = 2, 4, 21, 26, 43, 44 + 16
//        내 랜덤 생성번호= 43, 21, 16, 44, 26, 4 + 1
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                if (Result.get(j) == Win.get(i) ) {
                    cnt++;
                    if(cnt == 5 && Result.get(j) == Win.get(6)){
                        bns = 1;
                    }
                }
            }
        }

        if (cnt == 3) {
            str = "3개 맞았습니다. 5등!";
        } else if (cnt == 4) {
            str = "4개 맞았습니다. 4등!";
        } else if (cnt == 5 && bns == 0) {
            str = "5개 맞았습니다. 3등!";
        } else if (cnt == 5 && bns == 1) {
            str = "5개 + 보너스!! 2등!.";
        } else if (cnt == 6) {
            str = "6개 맞았습니다. 1등!츄카포카";
        } else if (cnt == 1) {
            str = "1개 맞았습니다 - 꽝 -";
        } else if (cnt == 2) {
            str = "2개 맞았습니다 - 꽝 -";
        } else {
            str = "꽝꽝꽝꽝꽝꽝";
        }
        return str;
    }

    //로또 번호 생성
    public void getLottoTicket() {

        Result = new ArrayList<>();//Result에 arraylist 초기화
        List<Integer> list = new ArrayList<Integer>();  //list에 arraylist 초기화

        // List 안에 1 ~ 45번 까지 로또번호 추가
        for (int i = 1; i <= 45; i++) {
            list.add(i);
        }

        // Result 안에 1 ~ 6번 까지 로또번호 랜덤 추가
        for (int i = 0; i < 6; i++) {
            final int idx = new Random().nextInt(list.size());
            Result.add(list.get(idx));
            list.remove(idx);
        }

        // 정렬
        Collections.sort(Result);//오름차순
        //Collections.reverse(list);//내림차순

        final int idx = new Random().nextInt(list.size());
        Result.add(list.get(idx));
        list.remove(idx);

        for(int i = 0; i < Result.size(); i++){
            Log.e("asdf", "Result = " + Result.get(i));
        }
    }

    //String 형식으로 형변환
    private String convertIntoString(List<Integer> change) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < change.size(); i++) {
            if (sb.length() > 0) {
                if (i == change.size() - 1) {
                    sb.append(" + ");
                } else {
                    sb.append(", ");
                }
            }
            sb.append(change.get(i));
        }
        return sb.toString();
    }

    //Single패턴 DB조회하기
//    void Winning_number() {

//         disposables.add(AppDatabase.getDatabase(this).getLottoDao().Frequency(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<Lotto>() {
//                    @Override
//                    public void onSuccess(Lotto lotto) {
//                        Log.e("Lottonum", "number" + lotto.drwNo + "," + lotto.bnusNo);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                }));
//    }

//    void getLotto() {
//        int num = Integer.parseInt(tv_result.getText().toString());
//        disposables.add(AppDatabase.getDatabase(this).getLottoDao().findLotto(num)
//                //getLotto(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<Lotto>() {
//
//                    @Override
//                    public void onSuccess(Lotto lotto) {
//                        lotto.getClass();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                }));
//    }


    //디비에서 가져오기
//    Single<Lotto> getLotto(int lottoNo) {
//        //여기에 로또 저장하기 넣기
//        //return Single.just(new Lotto());
//        return dao.findLotto(lottoNo);
//    }
//
//    Single<List<Lotto>> getLottos(int lottoNo) {
//        //여기에 로또 저장하기 넣기
//        ArrayList<Lotto> list = new ArrayList<>();
//        list.add(new Lotto());
//        return Single.just(list);
//    }

    void callAPIs() {

        int i = Integer.parseInt(tv_result.getText().toString());
        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);
        Call<Lotto> commentStr = apiService.getComment(i);
        commentStr.enqueue(new Callback<Lotto>() {
            @Override
            public void onResponse(Call<Lotto> call, Response<Lotto> response) {
                boolean isSuccessful = response.isSuccessful();
                if (isSuccessful) {
                    Lotto lotto = response.body();
                    ArrayList<Integer>temp = new ArrayList<>();
                    temp.add(lotto.drwtNo1);
                    temp.add(lotto.drwtNo2);
                    temp.add(lotto.drwtNo3);
                    temp.add(lotto.drwtNo4);
                    temp.add(lotto.drwtNo5);
                    temp.add(lotto.drwtNo6);
                    temp.add(lotto.bnusNo);
                    // Result 안에 1 ~ 6번 까지 로또번호 랜덤 추가
                    for (int n = 0; n < temp.size(); n++) {
                        Log.e("asd", "temp = " + temp.get(n));
                    }
                    //todo: 받아온정보를 내가가진 로또번호와 비교 -> 몇등?
                    //todo: 등수를 팝업으로 표시
                    show(temp);
                }
            }
            @Override
            public void onFailure(Call<Lotto> call, Throwable t) {
            }
        });
    }
}




