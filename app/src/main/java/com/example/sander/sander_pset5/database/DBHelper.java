package com.example.sander.sander_pset5.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sander on 6-5-17.
 *
 * Describes how the database is to be created.
 */

class DBHelper extends SQLiteOpenHelper {

    /* Static strings */
    // database name
    public static final String DATABASE_NAME = "Todo.db";

    // database version
    public static final int DATABASE_VERSION = 1;

    // table name
    public static final String TABLE = "todo";

    // column names
    public static final String _ID = "_id";
    public static final String LIST_ID = "list_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CHECKED = "checked";

    // table creation query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIST_ID + " INTEGER NOT NULL, " +
                    TITLE + " TEXT NOT NULL, " +
                    DESCRIPTION + " TEXT NOT NULL, " +
                    CHECKED + " INTEGER NOT NULL);";

    // constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("log", "DBHelper.onCreate: success");
    }

    // upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
    }
}
