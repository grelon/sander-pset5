package com.example.sander.sander_pset5;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvLists;
    ArrayAdapter todoListArrayAdapter;
    ArrayList<String> listTitles;
    ArrayList<TodoList> lists;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.main_title);

        Log.d("log", "Main.onCreate: start");

        // get databasehelper
        db = db.getInstance(this);

        // get views
        lvLists = (ListView) findViewById(R.id.lvMainLists);

        // testing: create test todolists
//        Todo test_todo = new Todo(0, "Testtitle", "Testdescription", 0);
//        ArrayList<Todo> test_todos = new ArrayList<>();
//        test_todos.add(test_todo);
//        Log.d("log", "Main.onCreate: test_todos(todo): " + test_todos.get(0).getTitle());
//        TodoList test_list = new TodoList("TestTitle", test_todos);
//        db.createList(test_list);

        // testing
        Log.d("log", "Database: ");
        lists = db.readLists();

        for (TodoList list: lists) {
            // Log.d("log", "List title:");
            ArrayList<Todo> todolist = list.getList();
            if (todolist == null) {
                Log.d("log", "todolist is null");
            }
            for (Todo todo : todolist) {
                Log.d("log", "Todo (ID: " + todo.getId() +") title:" + todo.getTitle());
            }
        }

        // update list with todolists
//        updateList();
        Log.d("log", "Main.onCreate: updateList() success");

    }

    // TODO: 12-5-17 Still need to test this properly
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        Log.d("log", "Main.onDestroy: db.close() success");
    }

    private void updateList() {
        Log.d("log", "Main.onCreate.updateList: start");

        // store all lists from db in lists
        lists = db.readLists();
        Log.d("log", "Main.onCreate.updateList: db.read() succes");

        // store titles of lists
        listTitles = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            listTitles.add(lists.get(i).getTitle());
        }

        // create adapter for lvLists
        todoListArrayAdapter = new ArrayAdapter<> (getApplicationContext(),
                android.R.layout.simple_list_item_1, listTitles);

        // set adapter to lvLists
        lvLists.setAdapter(todoListArrayAdapter);
    }
}
