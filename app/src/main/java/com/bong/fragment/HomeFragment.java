package com.bong.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bong.fragment.data.Apiservice;
import com.bong.fragment.data.Lotto;
import com.bong.fragment.network.RetrofitMaker;
import com.bong.fragment.ui.History.HistoryFragment;
import com.bong.fragment.ui.Trend.TrendFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private EditText tv_result;
    private TextView tv_generate;
    List<Integer>Result;
    boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity)getActivity()).Toolbar(1);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Bundle extra = this.getArguments();
        ArrayList<String>list = extra.getStringArrayList("list");
        ArrayList<String>str = extra.getStringArrayList("str");
        ArrayList<Integer>pre = extra.getIntegerArrayList("pre");

        //히스토리 버튼
        Button history = getView().findViewById(R.id.bt_history);
        history.setOnClickListener(v -> {
            Fragment fragmenthistory;
            Bundle args = new Bundle();
            args.putStringArrayList("list", list);
            args.putStringArrayList("str", str);
            fragmenthistory = new HistoryFragment();
            fragmenthistory.setArguments(args);
            ((MainActivity)getActivity()).changeFragment(MainActivity.Type.history, fragmenthistory);
        });

        //트렌드 버튼
        Button trend = getView().findViewById(R.id.bt_trend);
        trend.setOnClickListener(v -> {
            Fragment fragmenttrend;
            Bundle args = new Bundle();
            args.putIntegerArrayList("pre", pre);
            fragmenttrend = new TrendFragment();
            fragmenttrend.setArguments(args);
            ((MainActivity)getActivity()).changeFragment(MainActivity.Type.trend, fragmenttrend);
        });

        //생성하기 버튼
        Button generate = getView().findViewById(R.id.bt_generate);
        generate.setEnabled(false);
        tv_result = getView().findViewById(R.id.tv_event_number);
        View view1 = getView().findViewById(R.id.v_result);
        tv_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                view1.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() != 0) {
                    generate.setEnabled(true);
                }else{
                    generate.setEnabled(false);
                }
            }
        });

        //생성버튼 클릭 리스너
        generate.setOnClickListener(v -> {
            String drwNo = tv_result.getText().toString();
            int num = Integer.parseInt(drwNo);
            if (num > 0 && num < 51) {
                view1.setVisibility(View.VISIBLE);
                showToast();
                getLottoTicket();
                tv_generate = getView().findViewById(R.id.tv_lotto);
                tv_generate.setText(convertIntoString(Result));
            }else{
                if (flag) {
                    //다이얼로그 띄우기(1보다 작고 50보다 크면 출력)
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.notification);
                    builder.setMessage(R.string.possible);
                    builder.setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), R.string.check, Toast.LENGTH_LONG);
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            flag = true;
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    flag = false;
                }
            }
            //결과보기 버튼
            Button result_bt = getView().findViewById(R.id.bt_match);
            result_bt.setOnClickListener(v1 -> {
                callAPIs();
            });
            flag = true;
        });
    }

    void callAPIs() {

        int i = Integer.parseInt(tv_result.getText().toString());
        Apiservice apiService = new RetrofitMaker().createService(getActivity(), Apiservice.class);
        Call<Lotto> commentStr = apiService.getComment(i);
        commentStr.enqueue(new Callback<Lotto>() {
            @Override
            public void onResponse(Call<Lotto> call, Response<Lotto> response) {
                boolean isSuccessful = response.isSuccessful();
                if (isSuccessful) {
                    Lotto lotto = response.body();
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(lotto.drwtNo1);
                    temp.add(lotto.drwtNo2);
                    temp.add(lotto.drwtNo3);
                    temp.add(lotto.drwtNo4);
                    temp.add(lotto.drwtNo5);
                    temp.add(lotto.drwtNo6);
                    temp.add(lotto.bnusNo);

                    show(temp);
                }
            }
            @Override
            public void onFailure(Call<Lotto> call, Throwable t) {
            }
        });
    }

    void show(List<Integer> Win){
        if (flag) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.result);

            String str = convertIntoString(Result);
            String str1 = convertIntoString(Win);
            String drwNo = tv_result.getText().toString();

            builder.setMessage("나의 번호 = [" + str + "]\n" + drwNo + "회번호 = [" + str1 + "]\n" + LottoRank(Win));
            builder.setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flag = true;
                    Toast.makeText(getActivity(),R.string.check, Toast.LENGTH_LONG).show();
                }
            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) { flag = true; }
            });
            builder.show();
            flag = false;
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
    }

    //토스트 메시지 띄우기
    void showToast() {
        Toast.makeText(this.getActivity(), R.string.generate_num, Toast.LENGTH_LONG).show();
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
}
