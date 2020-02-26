package com.bong.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

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

    private Fragment fragmenthistory;
    private Fragment fragmenttrend;
    private EditText tv_result;
    private TextView tv_generate;
    private Button result_bt;
    private Button generateBtn;
    List<Integer>Result;
    boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle extra = this.getArguments();
        ArrayList<String>list = extra.getStringArrayList("list");
        for(int i = 0; i < list.size(); i++){
            if (list != null)
                Log.e("home list", "home list = " + list.get(i));
        }
        ArrayList<String>str = extra.getStringArrayList("str");
        for(int i = 0; i < str.size(); i++){
            if(str != null){
                Log.e("home str", "home str = " + str.get(i));
            }
        }
        ArrayList<Integer>pre = extra.getIntegerArrayList("pre");
        for(int i = 0; i < pre.size(); i++){
            if(pre != null){
                Log.e("home pre", "home pre = " + pre.get(i));
            }
        }

        //히스토리 버튼
        Button history = getView().findViewById(R.id.bt_history);
        history.setOnClickListener(v -> {

            Bundle args = new Bundle();
            args.putStringArrayList("list", list);
            args.putStringArrayList("str", str);
            fragmenthistory = new HistoryFragment();
            fragmenthistory.setArguments(args);
            ((MainActivity)getActivity()).ChangeFragment(2, fragmenthistory);

        });

        //트렌드 버튼
        Button trend = getView().findViewById(R.id.bt_trend);
        trend.setOnClickListener(v -> {

            Bundle args = new Bundle();
            args.putIntegerArrayList("pre", pre);
            fragmenttrend = new TrendFragment();
            fragmenttrend.setArguments(args);
            ((MainActivity)getActivity()).ChangeFragment(2, fragmenttrend);

        });

        //결과보기 버튼
        result_bt = getView().findViewById(R.id.bt_match);

        //번호 생성 버튼
        generateBtn = getView().findViewById(R.id.bt_generate);
        generateBtn.setOnClickListener(v -> {
            View view1 = getView().findViewById(R.id.v_result);
            view1.setVisibility(View.VISIBLE);
            showToast();
            getLottoTicket();
            tv_generate = getView().findViewById(R.id.tv_lotto);
            tv_generate.setText(convertIntoString(Result));

            //EditText값 가져오기
            tv_result = getView().findViewById(R.id.tv_event_number);
            tv_result.getText().toString();

        });

        tv_result = getView().findViewById(R.id.tv_event_number);
        tv_result.addTextChangedListener(new TextWatcher() {
            //입력전
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //입력되는 텍스트에 변화가 있을때
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            //입력후
            @Override
            public void afterTextChanged(Editable s) {

                String text = s.toString();
                if (text.length() != 0) {
                    result_bt.setEnabled(true);
                    generateBtn.setEnabled(true);//버튼 활성화
                    String drwNo = tv_result.getText().toString();
                    int num = Integer.parseInt(drwNo);
                    if (num > 0 && num < 51) {
                        result_bt.setOnClickListener(v -> {
                            callAPIs();
                        });
                    } else {
                        result_bt.setOnClickListener(v -> {
                            if (flag) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("알림");
                                builder.setMessage("회차 번호는 1회부터 50회까지만 가능 합니다.");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        flag = false;
                                        Toast.makeText(getActivity(), "확인", Toast.LENGTH_LONG);
                                    }
                                });
                                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        flag = true;
                                    }
                                });
                                builder.show();
                                flag = false;
                            }
                        });
                    }
                } else {
                    result_bt.setEnabled(false);//버튼 비활성
                    generateBtn.setEnabled(false);
                }
            }
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
        if(flag){

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
            builder.setTitle("결과보기");

            String str = convertIntoString(Result);
            String str1 = convertIntoString(Win);
            String drwNo = tv_result.getText().toString();

            builder.setMessage("나의 번호 = [" + str + "]\n" + drwNo + "회번호 = [" + str1 + "]\n" + LottoRank(Win));
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    flag = true;
                    Toast.makeText(getActivity(),"확인", Toast.LENGTH_LONG).show();
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

        for(int i = 0; i < 7; i++) {
            Log.e("sdf", "lotto = " + Result.get(i));
        }
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
