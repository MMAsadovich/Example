package com.example.exaample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import static com.example.exaample.MainActivity.notifyTitle;

public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        TextView textView = findViewById(R.id.textView);
        textView.setText(notifyTitle);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(NotifyActivity.this, MainActivity.class));
                finish();
            }
        },2000);
    }
}