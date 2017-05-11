package com.example.sander.sander_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

/**
 * Created by sander on 11-5-17.
 *
 * Defines database and CRUD methods. Instantiate database using getInstance().
 */

class DBHelper extends SQLiteOpenHelper {

    /* Static values */

    // empty instance to be filled
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;

    // database name
    private static final String DATABASE_NAME = "Todo.db";

    // database version
    private static final int DATABASE_VERSION = 1;

    // table name
    private static final String TABLE = "todo";

    // column names
    private static final String _ID = "_id";
    private static final String LIST_ID = "list_id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String CHECKED = "checked";

    // table creation query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIST_ID + " INTEGER NOT NULL, " +
                    TITLE + " TEXT NOT NULL, " +
                    DESCRIPTION + " TEXT NOT NULL, " +
                    CHECKED + " INTEGER NOT NULL);";


    /**
     * Methods for instantiation and upgrading of db
     * and dbHelper
     */

    // use getInstance() to instantiate dbHelper
    static synchronized DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context.getApplicationContext());
        }
        db = dbHelper.getWritableDatabase();
        return dbHelper;
    }


    // DON'T use this constructor to instantiate dbHelper!
    private DBHelper(Context context) {
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


    /**
     * CRUD Methods
     */

    public void create(Todo todo) {
        // insert values into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.LIST_ID, todo.getList_id());
        contentValues.put(DBHelper.TITLE, todo.getTitle());
        contentValues.put(DBHelper.DESCRIPTION, todo.getDescription());
        contentValues.put(DBHelper.CHECKED, todo.getChecked());
        db.insert(DBHelper.TABLE, null, contentValues);
    }

    public TodoList<Todo> read() {
        // create arraylist we can fill with todos
        TodoList<Todo> todos = new TodoList<>();

        // define which columns we want to read
        String[] columns = new String[] {
                DBHelper._ID,
                DBHelper.LIST_ID,
                DBHelper.TITLE,
                DBHelper.DESCRIPTION,
                DBHelper.CHECKED };

        // create cursor object filled with previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE, columns, null, null, null, null, null);

        // move over rows in cursor,
        if (cursor.moveToFirst()) {
            do {
                // initialize todo-object
                Todo todo = new Todo();

                // set attributes
                todo.setId(cursor.getInt(cursor.getColumnIndex(DBHelper._ID)));
                todo.setList_id(cursor.getInt(cursor.getColumnIndex(DBHelper.LIST_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)));
                todo.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION)));
                todo.setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.CHECKED)));

                // create todo object from received data
                todos.add( todo);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }
        cursor.close();
        return todos;
    }

    public int update(Todo todo) {
        // update values and return exit code
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TITLE, todo.getTitle());
        contentValues.put(DBHelper.DESCRIPTION, todo.getDescription());
        contentValues.put(DBHelper.CHECKED, todo.getChecked());

        return db.update(DBHelper.TABLE, contentValues, DBHelper._ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
    }

    public void delete(Todo todo) {
        // delete todo from database
        db.delete(DBHelper.TABLE, DBHelper._ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
    }
}
