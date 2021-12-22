package com.example.save_your_time;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    public void openDiary(View view){
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void openStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case (R.id.blockScreen):
                Intent intent = new Intent(this, BlockFullActivity.class);
                startActivity(intent);
                return true;
        }
        return true;
    }


}