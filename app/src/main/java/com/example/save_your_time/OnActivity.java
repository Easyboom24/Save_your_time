package com.example.save_your_time;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class OnActivity extends AppCompatActivity {

    TextView textView;
    int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on);
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        String datetimestart = mDay + "." + mMonth + "." + mYear + " " + mHour + ":" + mMinute;
        String datetimeend = mDay + "." + mMonth + "." + mYear + " " + (((mHour + 1) < 25) ? mHour + 1 : 0)  + ":" + mMinute;
        TextView start = findViewById(R.id.start);
        start.setText(datetimestart);
        TextView end = findViewById(R.id.end);
        end.setText(datetimeend);
    }

    public void callDatePicker(View view) {
        // получаем текущую дату
        int id = view.getId();
        if (id == R.id.startClick)
            textView = findViewById(R.id.start);
        else
            textView = findViewById(R.id.end);
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);


        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                        callTimePicker(date, textView);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void callTimePicker(String date, TextView textView) {
        // получаем текущее время
        final Calendar cal = Calendar.getInstance();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        // инициализируем диалог выбора времени текущими значениями
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        String datetime = date + " " + time;
                        textView.setText(datetime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }



    //Вовод списка выбора повтора(каждую неделю/день)
    public void getAgainList(View view){
        TextView textView = findViewById(R.id.again);
        textView.setText("Всегда");

    }

    public void startNow(View view) {
        CheckBox checkBox = (CheckBox) view;
        boolean checked = checkBox.isChecked();
        LinearLayout start = findViewById(R.id.startClick);
        if (checked) {
            start.setVisibility(View.GONE);
        }
        else {
            start.setVisibility(View.VISIBLE);
        }
    }
}