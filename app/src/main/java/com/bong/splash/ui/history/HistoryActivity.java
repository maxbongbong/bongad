package com.bong.splash.ui.history;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bong.splash.R;
import com.bong.splash.data.Lotto;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HistoryActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    LottoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        disposables = new CompositeDisposable();

        dao = AppDatabase.getDatabase(this).getLottoDao();


    }

    void WinningNumber(){
        disposables.add(AppDatabase.getDatabase(this).getLottoDao().loadAllLottos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {
                    @Override
                    public void onSuccess(Lotto lotto) {
                        Log.e("history", "lotto_history" + lotto);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }));
    }

}
