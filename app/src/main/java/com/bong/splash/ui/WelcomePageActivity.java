package com.bong.splash.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bong.splash.R;

public class WelcomePageActivity extends AppCompatActivity {
    public static void launch(Context context, String message) {
        Intent intent = new Intent(context, WelcomePageActivity.class);
        intent.putExtra("message", message);
        context.startActivity(intent); //로딩이 끝난 후, ChoiceFunction 이동
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_page);
        Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                startActivity(intent);
            }
        });

        TextView tv_welcome = findViewById(R.id.tv_welcome);
        Intent intent = getIntent();
        if (intent != null) {
            String messsage = intent.getStringExtra("message");
            if (messsage != null) {
                tv_welcome.setText(messsage);
            }
        }
    }
}
