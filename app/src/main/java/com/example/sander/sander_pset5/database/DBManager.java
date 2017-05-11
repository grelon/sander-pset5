package com.example.sander.sander_pset5.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;


/**
 * Created by sander on 6-5-17.
 *
 * Creates database and defines CRUD methods
 */

public class DBManager {
    private DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    // constructor
    public DBManager(Context c) {
        context = c;
    }

    // open database, use before first CRUD
    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

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
                Todo todo;

                // set attributes
                todo.setId(cursor.getInt(cursor.getColumnIndex(DBHelper._ID)));
                todo.setList_id(cursor.getInt(cursor.getColumnIndex(DBHelper.LIST_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)));
                todo.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.DESCRIPTION)));
                todo.setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.CHECKED)));

                // create todo object from received data
                todos.add(todo);
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
