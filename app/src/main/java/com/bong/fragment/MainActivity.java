package com.bong.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bong.fragment.ui.History.HistoryFragment;
import com.bong.fragment.ui.Splash.SplashFragment;
import com.bong.fragment.ui.Trend.TrendFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_PERMISSION_GRANTED = 101;
    private static final int MESSAGE_PERMISSION_DENIED = 102;
    private Button button;
    private TextView textView;

//    public MainHandler mainHandler = new MainHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        button = (Button)findViewById(R.id.button);
        Fragment splashFragment;
        splashFragment = new SplashFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentFrame, splashFragment);
        ft.commit();
    }

    public enum Type {
        home, trend, history
    }
    public void changeFragment(Type type, Fragment fragment){

        //화면 전환 프레그먼트 선언 및 초기화면 설정
        //프레그먼트 매니저로 추가, 삭제, 대체 가능
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right_left, R.anim.fragment_close_exit);
        if (type.ordinal() == 0) {
            transaction.replace(R.id.contentFrame, fragment).commit();
        }else if(type.ordinal() > 0){
            //해당 transaction 을 Back Stack 에 저장
            transaction.addToBackStack(null);
            transaction.replace(R.id.contentFrame, fragment).commit();
        }
    }
}

