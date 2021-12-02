package com.example.save_your_time;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class OnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on);
        Calendar date = new GregorianCalendar();
    }

    protected void getDateTime(View view){

    }

    //Вовод списка выбора повтора(каждую неделю/день)
    protected void getAgainList(View view){

    }
}