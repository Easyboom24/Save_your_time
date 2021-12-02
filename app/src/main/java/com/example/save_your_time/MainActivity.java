package com.example.save_your_time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Главная");
    }

    public void openBlockSettings(View view) {
        Intent intent = new Intent(this, BlockSettingsActivity.class);
        startActivity(intent);
    }

    public void openOn(View view) {
        Intent intent = new Intent(this, OnActivity.class);
        intent.putExtra("mode", view.getId());
        startActivity(intent);
    }
}