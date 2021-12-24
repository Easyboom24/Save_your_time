package com.example.save_your_time;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OnActivity extends AppCompatActivity {

    TextView textView;
    boolean nowStart = false;
    int mYear, mMonth, mDay, mHour, mMinute, mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        String datetimestart = (mDay < 10 ? "0" + mDay : mDay) + "." + (mMonth < 10 ? "0" + mMonth : mMonth)
                + "." + mYear + " " + (mHour < 10 ? "0" + mHour : mHour) + ":" + (mMinute < 10 ? "0" + mMinute : mMinute);
        String datetimeend = (mDay < 10 ? "0" + mDay : mDay) + "." + (mMonth < 10 ? "0" + mMonth : mMonth)
                + "." + mYear + " " + (((mHour + 1) < 25) ? mHour + 1 : 0)  + ":" + (mMinute < 10 ? "0" + mMinute : mMinute);
        TextView start = findViewById(R.id.start);
        start.setText(datetimestart);
        TextView end = findViewById(R.id.end);
        end.setText(datetimeend);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);
        if (mode != R.id.planWork) {
            nowStart = true;
            setTitle((mode == R.id.block) ? "Блокировка" : "\"Без телефона\"");
            findViewById(R.id.modeField).setVisibility(View.GONE);
            findViewById(R.id.startClick).setVisibility(View.GONE);
            findViewById(R.id.againField).setVisibility(View.GONE);
        }
        else {
            setTitle("Плановая работа");
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void writeOrStart(View view)
    {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Общие переменные
        TextView end = findViewById(R.id.end);
        String endDate = end.getText().toString();
        EditText topicView = findViewById(R.id.theme);
        String topic = topicView.getText().toString();
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        String nowDate = (mDay < 10 ? "0" + mDay : mDay) + "." + (mMonth < 10 ? "0" + mMonth : mMonth)
                + "." + mYear + " " + (mHour < 10 ? "0" + mHour : mHour) + ":" + (mMinute < 10 ? "0" + mMinute : mMinute);
        if (nowStart)
        {
            /*String modeString = (mode == R.id.without) ? "Без телефона" : "Блокировка";
            db.execSQL("INSERT INTO " + DBHelper.TABLE_FUNCTION + "(" +
                    DBHelper.FUNCTION_NAME + "," +
                    DBHelper.FUNCTION_START + "," +
                    DBHelper.FUNCTION_INTERVAL +
                    DBHelper.FUNCTION_COMPLETED + ") VALUES(\"" +
                    modeString + "\", \"" + nowDate + "\", \"" + endDate + "\", " + "0" + ")");

            Cursor added = db.rawQuery("SELECT " + DBHelper.FUNCTION_ID + " FROM " + DBHelper.TABLE_FUNCTION +
                    " WHERE " + DBHelper.FUNCTION_ID + "=last_insert_rowid()" , null);
            added.moveToNext();
            int toDiary = added.getInt(0);
            added.close();
            db.execSQL("INSERT INTO " + DBHelper.TABLE_DIARY + "(" +
                    DBHelper.DIARY_FUNCTION_ID + ", " +
                    DBHelper.DIARY_TOPIC + ", " +
                    DBHelper.DIARY_DATE_CHANGE + ") VALUES (" +
                    toDiary + ", \"" + topic + "\",  \"" + nowDate + "\");");*/

            /*NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if ( notificationManager.isNotificationPolicyAccessGranted()) {
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                // Ask the user to grant access
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, 1);
            }*/

            //После записи в БД запускается сервис с функцией

                startService(new Intent(this, WithoutService.class));

        }
        else
        {
            TextView startDate = findViewById(R.id.start);
            RadioGroup radioGroup = findViewById(R.id.mode);
            RadioButton checkedMode = findViewById(radioGroup.getCheckedRadioButtonId());
            String modeString = checkedMode.getText().toString();

            HashMap<String, Boolean> again = new HashMap();
            //Дни недели
            again.put("Пн", ((CheckBox)(findViewById(R.id.mon))).isChecked());
            again.put("Вт", ((CheckBox)(findViewById(R.id.tue))).isChecked());
            again.put("Ср", ((CheckBox)(findViewById(R.id.wed))).isChecked());
            again.put("Чт", ((CheckBox)(findViewById(R.id.thu))).isChecked());
            again.put("Пт", ((CheckBox)(findViewById(R.id.fri))).isChecked());
            again.put("Сб", ((CheckBox)(findViewById(R.id.sat))).isChecked());
            again.put("Вс", ((CheckBox)(findViewById(R.id.sun))).isChecked());

            String againString = "";
            for(Map.Entry<String, Boolean> entry : again.entrySet()) {
                String key = entry.getKey();
                Boolean value = entry.getValue();
                if (value)
                    againString += key + ",";
            }
            if (againString == "")
                againString = "Никогда";
            else
                againString = againString.substring(0, againString.length() - 2);
            db.execSQL("INSERT INTO " + DBHelper.TABLE_FUNCTION + "(" +
                    DBHelper.FUNCTION_NAME + ", " +
                    DBHelper.FUNCTION_START + ", " +
                    DBHelper.FUNCTION_INTERVAL + ", " +
                    DBHelper.FUNCTION_COMPLETED + ") VALUES(\"" +
                    modeString + "\", \"" + startDate.getText() + "\", \"" + endDate + "\", " + "0" + ")");
            Cursor added = db.rawQuery("SELECT " + DBHelper.FUNCTION_ID + " FROM " + DBHelper.TABLE_FUNCTION +
                    " WHERE " + DBHelper.FUNCTION_ID + "=last_insert_rowid()" , null);
            added.moveToNext();
            int toDiary = added.getInt(0);
            added.close();
            db.execSQL("INSERT INTO " + DBHelper.TABLE_DIARY + "(" +
                    DBHelper.DIARY_FUNCTION_ID + ", " +
                    DBHelper.DIARY_TOPIC + ", " +
                    DBHelper.DIARY_DATE_CHANGE + ") VALUES (" +
                    toDiary + ", \"" + topic + "\",  \"" + nowDate + "\");");

            db.execSQL("INSERT INTO " + DBHelper.TABLE_PLANNED + "(" +
                    DBHelper.PLANNED_REPEAT + ", " + DBHelper.PLANNED_FUNCTION_ID + ") VALUES (\"" +
                    againString + "\", " + toDiary + ")");

            db.close();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
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