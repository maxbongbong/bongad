package com.bong.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bong.fragment.ui.Splash.SplashFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment splashFragment;
        splashFragment = new SplashFragment();
        Toolbar(0);
        changeFragment(Type.splash, splashFragment);
    }
    public void Toolbar(int num){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switch (num){
            case 0:
                getSupportActionBar().hide();
                break;
            case 1:
                getSupportActionBar().show();
                getSupportActionBar().setTitle(R.string.title_main);
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.title_history);
                break;
            case 3:
                getSupportActionBar().setTitle(R.string.title_trend);
                break;
        }
    }

    public enum Type {
        splash, home, trend, history
    }
    public void changeFragment(Type type, Fragment fragment){

        //화면 전환 프레그먼트 선언 및 초기화면 설정
        //프레그먼트 매니저로 추가, 삭제, 대체 가능
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right_left, R.anim.fragment_close_exit);
        if (type.ordinal() <=1) {
            transaction.replace(R.id.contentFrame, fragment).commit();
        }else if(type.ordinal() > 1){
            //해당 transaction 을 Back Stack 에 저장
            transaction.addToBackStack(null);
            transaction.replace(R.id.contentFrame, fragment).commit();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return super.onCreateDialog(id);
    }
}

