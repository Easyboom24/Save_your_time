package com.example.save_your_time;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class OneDiaryActivity extends AppCompatActivity {

    TextView textView;
    int mYear, mMonth, mDay, mHour, mMinute;
    boolean newDiary = true;
    int id;
    String topic;
    String text;
    String date;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_diary);

        Intent intent = getIntent();
        id = intent.getIntExtra("idDiary", -1);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        if (id != -1)
        {
            newDiary = true;
            Cursor query = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DIARY + " WHERE " + DBHelper.DIARY_ID + "=" + id, null);
            query.moveToNext();
            topic = query.getString(1);
            text = query.getString(2);
            date = query.getString(3);

            EditText topicView = (EditText)findViewById(R.id.topic);
            topicView.setText(topic);

            EditText textView = (EditText)findViewById(R.id.text);
            textView.setText(text);

            TextView dataView = (TextView)findViewById(R.id.changeDate);
            dataView.setText(date);

            query.close();
        }
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
                        String date = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)
                                + "." + (monthOfYear + 1 < 10 ? "0" + monthOfYear + 1 : monthOfYear + 1)
                                + "." + year;
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
                        String time = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay)
                                + ":" + (minute < 10 ? "0" + minute : minute);
                        String datetime = date + " " + time;
                        textView.setText(datetime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void saveData(View view) {
        EditText topicView = (EditText)findViewById(R.id.topic);
        EditText textView = (EditText)findViewById(R.id.text);
        TextView dataView = (TextView)findViewById(R.id.changeDate);

        //db.execSQL("UPDATE ");
    }
}