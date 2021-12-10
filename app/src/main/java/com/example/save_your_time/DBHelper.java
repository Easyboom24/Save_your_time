package com.example.save_your_time;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyAppDB";

    public static  final  String TABLE_APP = "app";
    public static  final  String APP_ID = "_id";
    public static  final  String APP_NAME = "name";
    public static  final  String APP_PATH = "path";
    public static  final  String APP_BLOCKED = "blocked";

    public static  final  String TABLE_APP_FUNCTION = "function_app";
    public static  final  String APP_FK = "app_id";
    public static  final  String FUNCTION_FK = "function_id";

    public static  final  String TABLE_FUNCTION = "function";
    public static  final  String FUNCTION_ID = "_id";
    public static  final  String FUNCTION_NAME = "name";
    public static  final  String FUNCTION_START = "start_time";
    public static  final  String FUNCTION_INTERVAL = "interval";
    public static  final  String FUNCTION_COMPLETED = "completed";

    public static  final  String TABLE_ACHIEVEMENT = "achievement";
    public static  final  String ACHIEVEMENT_ID = "_id";
    public static  final  String ACHIEVEMENT_NAME = "name";
    public static  final  String ACHIEVEMENT_FUNCTION_ID = "function_id";
    public static  final  String ACHIEVEMENT_DATE_ISSUE = "date_issue";
    public static  final  String ACHIEVEMENT_POINTS = "points";

    public static  final  String TABLE_DIARY = "diary";
    public static  final  String DIARY_ID = "_id";
    public static  final  String DIARY_FUNCTION_ID = "function_id";
    public static  final  String DIARY_TOPIC = "topic";
    public static  final  String DIARY_TEXT = "text";
    public static  final  String DIARY_DATE_CHANGE = "date_change";

    public static  final  String TABLE_ACTIVITY = "activity";
    public static  final  String ACTIVITY_ID = "_id";
    public static  final  String ACTIVITY_APP_ID = "app_id";
    public static  final  String ACTIVITY_START = "activity_start";
    public static  final  String ACTIVITY_END = "activity_end";

    public static  final  String TABLE_PLANNED = "planned_work";
    public static  final  String PLANNED_ID = "_id";
    public static  final  String PLANNED_FUNCTION_ID = "function_id";
    public static  final  String PLANNED_REPEAT = "repeat";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_APP + "("
                + APP_ID + " INTEGER PRIMARY KEY," + APP_NAME + " TEXT," + APP_PATH + " TEXT,"
                + APP_BLOCKED + " NUMERIC" + ")"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FUNCTION + "("
                + FUNCTION_ID + " INTEGER PRIMARY KEY," + FUNCTION_NAME + " TEXT,"
                + FUNCTION_START + " TEXT," + FUNCTION_INTERVAL + " TEXT,"
                + FUNCTION_COMPLETED + " NUMERIC" + ")"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DIARY + "("
                + DIARY_ID + " INTEGER PRIMARY KEY,"
                + DIARY_TOPIC + " TEXT,"
                + DIARY_TEXT + " TEXT,"
                + DIARY_DATE_CHANGE + " TEXT,"
                + DIARY_FUNCTION_ID + " INTEGER, FOREIGN KEY (" + DIARY_FUNCTION_ID + ") "
                + "REFERENCES " + TABLE_FUNCTION + "(" + FUNCTION_ID + "))"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY + "("
                + ACTIVITY_ID + " INTEGER PRIMARY KEY,"
                + ACTIVITY_START + " TEXT,"
                + ACTIVITY_END + " TEXT,"
                + ACTIVITY_APP_ID + " INTEGER NOT NULL, FOREIGN KEY (" + ACTIVITY_APP_ID + ") "
                + "REFERENCES " + TABLE_APP + "(" + APP_ID + "))"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLANNED + "("
                + PLANNED_ID + " INTEGER PRIMARY KEY,"
                + PLANNED_REPEAT + " TEXT,"
                + PLANNED_FUNCTION_ID + " INTEGER NOT NULL, FOREIGN KEY ("
                + PLANNED_FUNCTION_ID + ") " + "REFERENCES " + TABLE_FUNCTION
                + "(" + FUNCTION_ID + "))"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ACHIEVEMENT + "("
                + ACHIEVEMENT_ID + " INTEGER PRIMARY KEY," + ACHIEVEMENT_NAME + " TEXT,"
                + ACHIEVEMENT_DATE_ISSUE + " TEXT," + ACHIEVEMENT_POINTS + " TEXT,"
                + ACHIEVEMENT_FUNCTION_ID + " INTEGER, FOREIGN KEY ("
                + ACHIEVEMENT_FUNCTION_ID + ") " + "REFERENCES " + TABLE_FUNCTION
                + "(" + FUNCTION_ID + "))"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_APP_FUNCTION + "("
                + FUNCTION_FK + " INTEGER NOT NULL,"
                + APP_FK + " INTEGER NOT NULL,"
                + " FOREIGN KEY (" + FUNCTION_FK + ") REFERENCES " + TABLE_FUNCTION
                + "(" + FUNCTION_ID + "), "
                + " FOREIGN KEY (" + APP_FK + ") REFERENCES " + TABLE_APP
                + "(" + APP_ID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
        onCreate(db);
    }
}
