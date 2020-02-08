package com.bong.splash.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bong.splash.R;
import com.bong.splash.data.Apiservice;
import com.bong.splash.data.Lotto;
import com.bong.splash.network.RetrofitMaker;
import com.bong.splash.room.AppDatabase;
import com.bong.splash.room.LottoDao;
import com.bong.splash.ui.main.MainPageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SplashActivity extends AppCompatActivity {

    protected CompositeDisposable disposables;
    LottoDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(this).getLottoDao();
        getLottosAndSave();
    }

void send(ArrayList<String> list, ArrayList<String> str, ArrayList<Integer> num){

        final int sub = 1001;

        Intent intent = new Intent(this, MainPageActivity.class);
        intent.putStringArrayListExtra("list", list);
        intent.putStringArrayListExtra("str", str);
        intent.putIntegerArrayListExtra("pre", num);
        startActivityForResult(intent, sub);

    }

    public void getLottosAndSave() {

        /**
         * 1. 로또 데이터 받아오기 : Retrofit
         * 2. 받아온 데이터 저장하기 : Room
         * 3. 1-50
         */
//        disposables.add(
//                Single.just(getString(R.string.welcome_title))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableSingleObserver<String>() {
//                            @Override
//                            public void onSuccess(String message) {
//                                Log.e("SplashBong","LottoThread: " + Thread.currentThread().getName());
//                                WelcomePageActivity.launch(SplashActivity.this, message);
//                                SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e("SplashBong","LottoThread: " + Thread.currentThread().getName());
//                            }
//                        })
//        );

        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);

        // 복수개 통신
        ArrayList<String>list = new ArrayList<>();
        ArrayList<String> str = new ArrayList<>();
        ArrayList<Integer>pre = new ArrayList<>();
        ArrayList<Single<Lotto>> temp = new ArrayList<Single<Lotto>>();
        for (int i = 1; i <= 50; i++) {
            temp.add(
                    apiService.getCommentRx(i)   //서버통신
                    // .subscribeOn(Schedulers.io())
                    //.map()//return 객체
                    //.flatMap()//single
                    .map(lotto -> {

                        ArrayList<Integer>num = new ArrayList<>();
                        ArrayList<Integer>win = new ArrayList<>();
                        num.add(lotto.drwNo);
                        win.add(lotto.drwtNo1);
                        win.add(lotto.drwtNo2);
                        win.add(lotto.drwtNo3);
                        win.add(lotto.drwtNo4);
                        win.add(lotto.drwtNo5);
                        win.add(lotto.drwtNo6);
                        win.add(lotto.bnusNo);

                        list.add("" + num.get(0));
                        for(int j = 0; j < win.size(); j++){
                            pre.add(win.get(j));
                        }
                        str.add(convertIntoString(win));

                        return lotto;
                    })
            );
        }

        final boolean add = disposables.add(Single.concat(temp)     //서버통신 + 디비저장 50번 하기를 실행시작
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Lotto>() {
                    @Override
                    public void onNext(Lotto lotto) {}
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        send(list, str, pre);
                        SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
                    }
                }));

        //클래스 따로 빼서 사용하는 법
//        AutoSave autoSave = new AutoSave();
//        disposables.add(autoSave.getLottoAndSave(this, 1, 50)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSubscriber<Lotto>() {
//                    @Override
//                    public void onNext(Lotto lotto) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        WelcomePageActivity.launch(SplashActivity.this, getString(R.string.app_name));
//                        SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
//                    }
//                }));


//        room에 lotto를 저장하기
//        disposables.add(saveLottoToRoomRx(new Lotto())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableCompletableObserver() {
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                }));

        //디비에서 가져오기
//        disposables.add(dao.findLotto(2)     //getLotto(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<Lotto>() {
//
//                    @Override
//                    public void onSuccess(Lotto lotto) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                }));


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
    //디비에서 가져오기
//    Single<Lotto> getLotto(int lottoNo) {
//        //여기에 로또 저장하기 넣기
//        //return Single.just(new Lotto());
//        return dao.findLotto(lottoNo);
//    }

//    Single<List<Lotto>> getLottos(int lottoNo) {
//        //여기에 로또 저장하기 넣기
//        ArrayList<Lotto> list = new ArrayList<>();
//        list.add(new Lotto());
//        return Single.just(list);
//    }

//    rxjava
//    Completable saveLottoToRoomRx(Lotto lotto) {
        //여기에 로또 저장하기 넣기

//        return Completable.complete();
//    }

    //normal
//    void saveLottoToRoom(Lotto lotto) {
//        //여기에 로또 저장하기 넣기
//        if (dao != null)
//            dao.insertLotto(lotto);
//    }

    /*void oneApi() {
        //1 개 통신
        Apiservice apiService = new RetrofitMaker().createService(this, Apiservice.class);
        disposables.add(apiService.getCommentRx(2)
                .subscribeOn(Schedulers.io()) //Single Rxjava 스트림을 Background Thread에서 실행
                .observeOn(AndroidSchedulers.mainThread())  //Single Rxjava 스트림의 결과를 UIThread(mainThread)에서 실행
                .subscribeWith(new DisposableSingleObserver<Lotto>() {
                    @Override
                    public void onSuccess(Lotto lotto) {
                        Log.e("SplashBong", "lotto: " + lotto.drwNo + "," + lotto.drwNoDate);


                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }*/


//    class AutoSave {
//        Flowable<Lotto> getLottoAndSave(Context context, int start, int end) {
//            Apiservice apiService = new RetrofitMaker().createService(context, Apiservice.class);
//
//            // 복수개 통신
//            ArrayList<Single<Lotto>> list = new ArrayList<>();
//            for (int i = 1; i <= 50; i++) {
//                list.add(
//                        apiService.getCommentRx(i)
//                                .subscribeOn(Schedulers.io())
//                                //.map()//return 객체
//                                //.flatMap()//single
//                                .map(lotto -> {
//                                    Log.e("SplashBong", "lotto 서버통신: " + lotto.drwNo + "," + lotto.drwNoDate);
//                                    saveLottoToRoom(lotto);
//                                    return lotto;
//                                })
//                );
//            }
//
//            return Single.concat(list);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables != null) {
            disposables.clear();
            disposables.dispose();
        }
    }
}



