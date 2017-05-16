package com.example.sander.sander_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;

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
    private static final String TODO_TEXT = "text";
    private static final String TODO_CHECKED = "checked";

    // table creation query
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE " + TABLE_TODOS + " ( " +
                    TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    TODO_LIST + " INTEGER NOT NULL, " +
                    TODO_TEXT + " TEXT, " +
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
        contentValues.put(DBHelper.TODO_LIST, todo.getList_id());
        contentValues.put(DBHelper.TODO_TEXT, todo.getText());
        contentValues.put(DBHelper.TODO_CHECKED, 0);
        db.insert(DBHelper.TABLE_TODOS, null, contentValues);
    }

    private ArrayList<Todo> readTodos(int list_id) {
        // create arraylist we can fill with todos
        ArrayList<Todo> todos = new ArrayList<>();

        // define which columns we want to read
        String[] columns = new String[] {
                DBHelper.TODO_ID,
                DBHelper.TODO_LIST,
                DBHelper.TODO_TEXT,
                DBHelper.TODO_CHECKED};

        // create cursor object filled with previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE_TODOS, null, DBHelper.TODO_LIST + " = ?",
                new String[] {String.valueOf(list_id)}, null, null, null);

        // move over rows in cursor,
        if (cursor.moveToFirst()) {
            do {
                // initialize todo-object
                Todo todo = new Todo();

                // set attributes
                todo.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_ID)));
                todo.setList_id(cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_LIST)));
                todo.setText(cursor.getString(cursor.getColumnIndex(DBHelper.TODO_TEXT)));
                todo.setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_CHECKED)));

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
        contentValues.put(DBHelper.TODO_TEXT, todo.getText());
        contentValues.put(DBHelper.TODO_CHECKED, todo.getChecked());

        return db.update(DBHelper.TABLE_TODOS, contentValues, DBHelper.TODO_ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
    }

    public void deleteTodo(Todo todo) {
        // delete todo from database
        db.delete(DBHelper.TABLE_TODOS, DBHelper.TODO_ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
    }


    /**
     * Lists table CRUD Methods
     */

    public void createList(TodoList list) {
        // insert list values into database
        ContentValues listValues = new ContentValues();
        listValues.put(DBHelper.LIST_TITLE, list.getTitle());
        db.insert(DBHelper.TABLE_LISTS, null, listValues);
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
                list.setList(readTodos(list.getId()));

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
