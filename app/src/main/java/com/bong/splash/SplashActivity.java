package com.bong.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.github.ybq.android.spinkit.style.Wave;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new SplashHandler(), 2000); // 1초 후에 hd handler 실행  3000ms = 3초
    }

    private class SplashHandler implements Runnable{
        public void run(){
            WelcomePageActivity.launch(SplashActivity.this, getString(R.string.welcome));
            SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }


    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }


}



