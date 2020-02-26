package com.bong.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bong.fragment.ui.History.HistoryFragment;
import com.bong.fragment.ui.Splash.SplashFragment;
import com.bong.fragment.ui.Trend.TrendFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Fragment splashFragment;
    Fragment historyFragment;
    Fragment trendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashFragment = new SplashFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentFrame, splashFragment);
        ft.commit();
    }

    public void ChangeFragment(int i, Fragment fragment){

        //화면 전환 프레그먼트 선언 및 초기화면 설정
        //프레그먼트 매니저로 추가, 삭제, 대체 가능
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (i == 1) {
            transaction.replace(R.id.contentFrame, fragment).commit();

        }else if(i == 2){
            //해당 transaction 을 Back Stack 에 저장
            transaction.addToBackStack(null);
            transaction.replace(R.id.contentFrame, fragment).commit();
        }
    }
}
