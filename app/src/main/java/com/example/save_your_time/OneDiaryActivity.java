package com.example.save_your_time;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OneDiaryActivity extends AppCompatActivity {

    boolean newDiary = true;
    int idDiary;
    String topic;
    String text;
    String date;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_diary);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        idDiary = intent.getIntExtra("idDiary", -1);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        if (idDiary != -1)
        {
            setTitle("Редактирование записи");
            newDiary = false;
            Cursor query = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DIARY + " WHERE " + DBHelper.DIARY_ID + "=" + idDiary, null);
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
        else{
            setTitle("Добавление записи");
        }
    }

    public void saveData(View view) {
        EditText topicView = (EditText)findViewById(R.id.topic);
        EditText textView = (EditText)findViewById(R.id.text);
        now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh.mm");

        if (newDiary)
        {
            db.execSQL("INSERT INTO " + DBHelper.TABLE_DIARY + "(" +
                    DBHelper.DIARY_TOPIC + ", " +
                    DBHelper.DIARY_TEXT + ", " +
                    DBHelper.DIARY_DATE_CHANGE + ") VALUES (\"" +
                    topicView.getText() + "\", \"" +
                    textView.getText() + "\", \"" + now.toString() + "\");");
        }
        else {
            db.execSQL("UPDATE " + DBHelper.TABLE_DIARY + " SET " +
                    DBHelper.DIARY_TOPIC + "= \"" + topicView.getText() + "\", " +
                    DBHelper.DIARY_TEXT + "= \"" + "" + textView.getText() + "\", " +
                    DBHelper.DIARY_DATE_CHANGE + "= \"" + now.toString() + "\" WHERE " +
                    DBHelper.DIARY_ID + "=" + idDiary + ";");
        }
        db.close();
        Intent intent = new Intent(this, DiaryActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}