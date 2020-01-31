package com.bong.splash.ui.history;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
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
import io.reactivex.Flowable;
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
    private ListView m_listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(this).getLottoDao();

//        listView = (ListView) findViewById(R.id.list_view);

        callAPIs();

//        Win.add("asdasd");
//        //adapter
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.item_drwno,R.id.WinNum, this.Win);
//
//        //adapter connection
//        ListView listView = (ListView)findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//        getLotto();
//        ArrayList<LottoView> data = new ArrayList<>();

//        data.add(new LottoView(1, 10));
//        data.add(new LottoView(2, 11));
//        data.add(new LottoView(3, 12));
//        data.add(new LottoView(4, 13));
//        data.add(new LottoView(5, 14));
//        data.add(new LottoView(6, 15));
//        data.add(new LottoView(7, 16));
//        data.add(new LottoView(8, 17));

        //adapter
//        MyFirstAdapter adapter = new MyFirstAdapter(data);

        //adapter connection
//        ListView listView = (ListView)findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//        callAPIs();
    }


//    void Test(){
//        List<Integer>data = new ArrayList<>();
//        for(int i = 1; i <= 50; i++){
//            data.add(i);
//        }
//
//        //adapter
//        MyFirstAdapter adapter = new MyFirstAdapter(data);
//
//        //adapter connection
//        ListView listView = (ListView)findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//    }


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

//    void getLotto(){
//
//        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);
//
//        // 복수개 통신
//        ArrayList<Single<Lotto>> list = new ArrayList<Single<Lotto>>();
//        for (int i = 1; i <= 50; i++) {
//            list.add(
//                    apiService.getCommentRx(i)   //서버통신
//                            // .subscribeOn(Schedulers.io())
//                            //.map()//return 객체
//                            //.flatMap()//single
//                            .map(lotto -> {
//                                Log.e("SplashBong", "lotto1 : " + lotto.drwNo);
//                                saveLottoToRoom(lotto);  //디비에 저장
//
//                                return lotto;
//                            })
//            );
//        }
//
//        //디비에서 가져오기
//        disposables.add(AppDatabase.getDatabase(this).getLottoDao().loadAllLottos()     //getLotto(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<Lotto>() {
//
//                    @Override
//                    public void onSuccess(Lotto lotto) {
//                        Log.e("asd", "lottonum = " + lotto.drwNo);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                }));
//    }
//
//    void saveLottoToRoom(Lotto lotto) {
//        //여기에 로또 저장하기 넣기
//        Log.e("SplashBong", "lotto3 : " + lotto.drwNo);
//        if (dao != null)
//            dao.insertLotto(lotto);
//    }

    void callAPIs() {

        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);
        for(int i = 0; i <= 49; i ++) {
            Call<Lotto> commentStr = apiService.getComment(i);
            commentStr.enqueue(new Callback<Lotto>() {
                private List<Integer> Winnum;
                private List<Integer> DrwNum;
                @Override
                public void onResponse(Call<Lotto> call, Response<Lotto> response) {
                    boolean isSuccessful = response.isSuccessful();
                    if (isSuccessful) {
                        Log.v("Test3", "isSuccessful:" + isSuccessful);
                        Lotto lotto = response.body();

                        this.DrwNum = new ArrayList<>();
                        DrwNum.add(lotto.drwNo + 1);
                        Collections.reverse(DrwNum);

                        // WinNum안에 로또추첨 번호 추가
                        this.Winnum = new ArrayList<>();
                        Winnum.add(lotto.drwtNo1);
                        Winnum.add(lotto.drwtNo2);
                        Winnum.add(lotto.drwtNo3);
                        Winnum.add(lotto.drwtNo4);
                        Winnum.add(lotto.drwtNo5);
                        Winnum.add(lotto.drwtNo6);
                        Winnum.add(lotto.bnusNo);

                        for (int n = 0; n < Winnum.size(); n++) {
                            Log.e("asd", "Winnum = " + Winnum);
                        }
                        for(int i = 0; i < DrwNum.size(); i++) {
                            Log.e("asd", "DrwNo = " + DrwNum.get(i));
                        }
                        List<String>list = new ArrayList<>();
                        list.add(convertIntoString(Winnum));

                        Log.e("asd", "list = " + list.get(0));
                        String [] str = new String[50];
                        for(int k =0; k < str.length; k++){
                            str[k] = list.get(0);
                        }

//                        String[] str = {"123", "123", "123", "123", "123", "123", "123","123", "123",};

                        int cnt = 0;
                        ArrayList<LottoNum> Data = new ArrayList<>();
                        for(int i = 0; i < 50; i++){
                            LottoNum item = new LottoNum();
                            item.LottoNum = "" + (i+1);

                            item.WinNum = str[cnt++];
                            Data.add(item);
                            if(cnt >= Winnum.size()) cnt = 0;
                        }

                        m_listView = (ListView)findViewById(R.id.list_view);
                        ListAdapter adapter = new LottoAdapter(Data);
                        m_listView.setAdapter(adapter);


                    }
                }
                @Override
                public void onFailure(Call<Lotto> call, Throwable t) {
                }
            });
        }
    }
}

