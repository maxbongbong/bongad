package com.bong.splash.ui.history;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HistoryActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    LottoDao dao;
    TextView Sub;
    TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        disposables = new CompositeDisposable();
//        Title = findViewById(R.id.TitleView);
        Sub = findViewById(R.id.SubView);
        dao = AppDatabase.getDatabase(this).getLottoDao();
        WinningNumber();
    }

    void WinningNumber(){
        List<Integer>WinNum = new ArrayList<>();
        List<Integer>DrwNo = new ArrayList<>();
        disposables.add(AppDatabase.getDatabase(this).getLottoDao().loadAllLottos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {
                    @Override
                    public void onSuccess(Lotto lotto) {
                        lotto.getClass();
                        DrwNo.add(lotto.drwNo);
                        WinNum.add(lotto.drwtNo1);
                        WinNum.add(lotto.drwtNo2);
                        WinNum.add(lotto.drwtNo3);
                        WinNum.add(lotto.drwtNo4);
                        WinNum.add(lotto.drwtNo5);
                        WinNum.add(lotto.drwtNo6);

                        Collections.sort(WinNum);

                        WinNum.add(lotto.bnusNo);

                        for(int i = 0; i < WinNum.size(); i++){
                            Log.e("history", "WinNum = " + WinNum.get(i));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                })
        );
        Sub.setText(convertIntoString(WinNum));
    }

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
}
