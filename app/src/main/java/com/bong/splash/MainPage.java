package com.bong.splash;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1) ;
    }
}

