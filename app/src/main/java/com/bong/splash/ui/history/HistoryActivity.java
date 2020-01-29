package com.bong.splash.ui.history;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bong.splash.R;
import com.bong.splash.data.Apiservice;
import com.bong.splash.data.Lotto;
import com.bong.splash.network.RetrofitMaker;
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
import io.reactivex.subscribers.DisposableSubscriber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    LottoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(this).getLottoDao();
        Test();
        getLotto();
    }

    void Test(){
        ArrayList<String>data = new ArrayList<>();
        for(int i = 0; i < 30; i++){
            data.add("data" + i);
        }

        //adapter
        MyFirstAdapter adapter = new MyFirstAdapter(data);

        //adapter connection
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
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

    void getLotto(){

        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);

        // 복수개 통신
        ArrayList<Single<Lotto>> list = new ArrayList<Single<Lotto>>();
        for (int i = 1; i <= 50; i++) {
            list.add(
                    apiService.getCommentRx(i)   //서버통신
                            // .subscribeOn(Schedulers.io())
                            //.map()//return 객체
                            //.flatMap()//single
                            .map(lotto -> {
                                Log.e("SplashBong", "lotto 서버통신: " + lotto.drwNo);
                                saveLottoToRoom(lotto);  //디비에 저장

                                return lotto;
                            })
            );
        }

        //디비에서 가져오기
        disposables.add(AppDatabase.getDatabase(this).getLottoDao().loadAllLottos()     //getLotto(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Lotto>() {

                    @Override
                    public void onSuccess(Lotto lotto) {
                        Log.e("asdfa", "drwsdfd" + lotto);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                }));
    }

    void saveLottoToRoom(Lotto lotto) {
        //여기에 로또 저장하기 넣기
        Log.e("SplashBong", "lotto 저장: " + lotto.drwNo);
        if (dao != null)
            dao.insertLotto(lotto);
    }
}
