package com.bong.fragment.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.bong.fragment.HomeFragment;
import com.bong.fragment.MainActivity;
import com.bong.fragment.R;
import com.bong.fragment.data.Apiservice;
import com.bong.fragment.data.Lotto;
import com.bong.fragment.network.RetrofitMaker;
import com.bong.fragment.room.AppDatabase;
import com.bong.fragment.room.LottoDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SplashFragment extends Fragment {

    protected CompositeDisposable disposables;
    private LottoDao dao;
    private Fragment homeFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        disposables = new CompositeDisposable();
        dao = AppDatabase.getDatabase(getActivity()).getLottoDao();
        getLottoAndSave();

        return rootView;
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

    void send(ArrayList<String> list, ArrayList<String> str, ArrayList<Integer> pre) {
        homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("list", list);
        args.putStringArrayList("str", str);
        args.putIntegerArrayList("pre", pre);
        homeFragment.setArguments(args);
    }

    public void getLottoAndSave() {
        Apiservice apiService = new RetrofitMaker().createService(getActivity(), Apiservice.class);

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> str = new ArrayList<>();
        ArrayList<Integer> pre = new ArrayList<>();

        // 복수개 통신
        ArrayList<Single<Lotto>> temp = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            temp.add(
                    apiService.getCommentRx(i)
                            .map(lotto -> {

                                ArrayList<Integer> num = new ArrayList<>();
                                ArrayList<Integer> win = new ArrayList<>();
                                num.add(lotto.drwNo);
                                win.add(lotto.drwtNo1);
                                win.add(lotto.drwtNo2);
                                win.add(lotto.drwtNo3);
                                win.add(lotto.drwtNo4);
                                win.add(lotto.drwtNo5);
                                win.add(lotto.drwtNo6);
                                win.add(lotto.bnusNo);

                                list.add("" + num.get(0));
                                for (int j = 0; j < win.size(); j++) {
                                    pre.add(win.get(j));
                                }
                                str.add(convertIntoString(win));

                                return lotto;
                            })
            );
        }

        final boolean add = disposables.add(Single.concat(temp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Lotto>() {
                    @Override
                    public void onNext(Lotto lotto) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                        send(list, str, pre);
                        ((MainActivity) getActivity()).changeFragment(MainActivity.Type.home, homeFragment);
                    }
                }));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (disposables != null) {
            disposables.clear();
            disposables.dispose();
        }
    }
}
