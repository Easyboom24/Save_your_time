package com.example.save_your_time;

import com.example.save_your_time.DBHelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class DiaryActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView textView;
    LinearLayout diaryField;
    LinearLayout block;

    LinearLayout info;
    TextView topicField;
    TextView textField;

    LinearLayout buttons;
    ImageButton edit;
    ImageButton delete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        setTitle("Дневник");

        DiaryActivity his = this;

        textView = findViewById(R.id.query);
        diaryField = findViewById(R.id.diaryField);

        dbHelper = new DBHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

       Cursor query = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_DIARY, null);
        while(query.moveToNext()) {
            int id = query.getInt(0);
            String topic = query.getString(1);
            String text = query.getString(2);
            textView.append("Topic: " + topic + " DateChange: " + text + "\n");

            block = new LinearLayout(this);
            block.setOrientation(LinearLayout.HORIZONTAL);
            int dp5 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
            block.setPadding(dp5, dp5, dp5, dp5);

            LinearLayout.LayoutParams blockparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            block.setLayoutParams(blockparams);

            info = new LinearLayout(this);
            info.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            infoparams.weight = 3;
            info.setLayoutParams(infoparams);

            topicField = new TextView(this);
            topicField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27);
            topicField.setText(topic);
            LinearLayout.LayoutParams topicparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            topicField.setLayoutParams(topicparams);

            textField = new TextView(this);
            textField.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27);
            textField.setTextColor(Color.parseColor("#ACACAC"));
            textField.setText(text);
            LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textField.setLayoutParams(textparams);

            info.addView(topicField);
            info.addView(textField);
            block.addView(info);

            buttons = new LinearLayout(this);
            LinearLayout.LayoutParams buttonsparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonsparams.gravity = Gravity.CENTER_VERTICAL;
            buttonsparams.weight = 1;
            buttons.setLayoutParams(buttonsparams);

            edit = new ImageButton(this);
            edit.setId(id);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(his, OneDiaryActivity.class);
                    intent.putExtra("idDiary", id);
                    startActivity(intent);
                }
            });
            int dp40 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams tobuttonparams = new LinearLayout.LayoutParams(dp40, dp40);
            edit.setLayoutParams(tobuttonparams);

            delete = new ImageButton(this);
            delete.setId(id);
            delete.setOnClickListener(new View.OnClickListener() {
                //Удалить запись
                @Override
                public void onClick(View v) {
                    db.execSQL("DELETE FROM " + DBHelper.TABLE_DIARY + "WHERE " + DBHelper.DIARY_ID + " = " + v.getId() + ";");
                }
            });
            delete.setLayoutParams(tobuttonparams);

            buttons.addView(edit);
            buttons.addView(delete);
            block.addView(buttons);

            diaryField.addView(block);
        }

        query.close();
        db.close();
    }
}
