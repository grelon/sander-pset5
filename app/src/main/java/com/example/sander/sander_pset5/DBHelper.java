package com.example.sander.sander_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;
import java.util.Iterator;

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


    /* Todos table definition */
    // todo table name
    private static final String TABLE_TODOS = "todo";

    // column names
    private static final String TODO_ID = "_id";
    private static final String TODO_LIST = "list_id";
    private static final String TODO_TITLE = "title";
    private static final String TODO_DESCRIPTION = "description";
    private static final String TODO_CHECKED = "checked";

    // table creation query
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE " + TABLE_TODOS + " ( " +
                    TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TODO_LIST + " INTEGER NOT NULL, " +
                    TODO_TITLE + " TEXT NOT NULL, " +
                    TODO_DESCRIPTION + " TEXT, " +
                    TODO_CHECKED + " INTEGER NOT NULL);";


    /* Lists table */
    // lists table name
    private static final String TABLE_LISTS = "lists";

    // column names
    private static final String LIST_ID = "_id";
    private static final String LIST_TITLE = "title";

    // table creation query
    private static final String CREATE_LISTS_TABLE =
            "CREATE TABLE " + TABLE_LISTS + " ( " +
                    LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LIST_TITLE + " TEXT NOT NULL);";





    /**
     * Methods for instantiation and upgrading of db
     * and dbHelper
     */

    // DON'T use this constructor to instantiate dbHelper!
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(CREATE_LISTS_TABLE);
    }

    // upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TODOS);
    }

    // use getInstance() to instantiate dbHelper
    static synchronized DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context.getApplicationContext());
        }
        db = dbHelper.getWritableDatabase();
        return dbHelper;
    }


    /**
     * Todo table CRUD Methods
     */

    public void createTodo(Todo todo) {
        // insert values into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.LIST_ID, todo.getList_id());
        contentValues.put(DBHelper.TODO_TITLE, todo.getTitle());
        contentValues.put(DBHelper.TODO_DESCRIPTION, todo.getDescription());
        contentValues.put(DBHelper.TODO_CHECKED, todo.getChecked());
        db.insert(DBHelper.TABLE_TODOS, null, contentValues);
    }

    private ArrayList<Todo> readTodos() {
        // create arraylist we can fill with todos
        ArrayList<Todo> todos = new ArrayList<>();

        // define which columns we want to read
        String[] columns = new String[] {
                DBHelper.TODO_ID,
                DBHelper.TODO_LIST,
                DBHelper.TODO_TITLE,
                DBHelper.TODO_DESCRIPTION,
                DBHelper.TODO_CHECKED};

        // create cursor object filled with previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE_TODOS, columns, null, null, null, null, null);
        Log.d("log", "readTodos(): cursor created");
        if (cursor != null) {Log.d("log", "cursor != null");}


        // move over rows in cursor,
        if (cursor.moveToFirst()) {
            Log.d("log", "if (cursor.moveToFirst()): passed");
            do {
                Log.d("log", "do {: passed");
                // initialize todo-object
                Todo todo = new Todo();

                // set attributes
                todo.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_ID)));
                todo.setList_id(cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_LIST)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.TODO_TITLE)));
                todo.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.TODO_DESCRIPTION)));
                todo.setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_CHECKED)));

                // testing
                Log.d("log", "todo.getTitle()" + todo.getTitle());

                // create todo object from received data
                todos.add(todo);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }
        cursor.close();
        return todos;
    }

    public int updateTodo(Todo todo) {
        // update values and return exit code
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TODO_TITLE, todo.getTitle());
        contentValues.put(DBHelper.TODO_DESCRIPTION, todo.getDescription());
        contentValues.put(DBHelper.TODO_CHECKED, todo.getChecked());

        return db.update(DBHelper.TABLE_TODOS, contentValues, DBHelper.TODO_ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
    }

    public void delete(Todo todo) {
        // delete todo from database
        db.delete(DBHelper.TABLE_TODOS, DBHelper.TODO_ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
    }


    /**
     * Lists table CRUD Methods
     */

    public void createList(TodoList list) {
        // insert values into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.LIST_TITLE, list.getTitle());
        db.insert(DBHelper.TABLE_LISTS, null, contentValues);
    }

    public ArrayList<TodoList> readLists() {
        // create arraylist we can fill with todolists
        ArrayList<TodoList> lists = new ArrayList<>();

        // define which columns we want to read
        String[] columns = new String[] {
                DBHelper.LIST_ID,
                DBHelper.LIST_TITLE };

        // create cursor object filled with previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE_LISTS, columns, null, null, null, null, null);

        // move over rows in cursor,
        if (cursor.moveToFirst()) {
            do {
                // initialize todo-object
                TodoList list = new TodoList();

                // set attributes
                list.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.LIST_ID)));
                list.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.LIST_TITLE)));

                // get todos for this list from todo table
                Log.d("log", "DBHelper.readLists() list.getID(): " + list.getId());
                list.setList(readTodos());

                // add list to array of lists
                lists.add(list);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }
        cursor.close();
        return lists;
    }

    public void updateList(TodoList list) {
        // prepare list values
        ContentValues listValues = new ContentValues();
        listValues.put(DBHelper.LIST_TITLE, list.getTitle());

        // update db
        db.update(DBHelper.TABLE_LISTS, listValues, DBHelper.LIST_ID + " = ?",
                new String[] {String.valueOf(list.getId())});
    }

    public void deleteList(TodoList list) {
        // delete list from database
        db.delete(DBHelper.TABLE_LISTS, DBHelper.LIST_ID + " = ?",
                new String[] {String.valueOf(list.getId())});

        // delete associated todos from db
        db.delete(DBHelper.TABLE_TODOS, DBHelper.TODO_LIST + " = ?",
                new String[] {String.valueOf(list.getId())});
    }

}
