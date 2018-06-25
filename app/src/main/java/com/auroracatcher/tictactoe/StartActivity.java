package com.auroracatcher.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void playOnline(View view) {
        Intent intent = new Intent(this, OfflineActivity.class);
        Toast.makeText(this, "hahaha", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public void playOffline(View view) {
        Intent intent = new Intent(this, OfflineActivity.class);
        startActivity(intent);
    }
}
