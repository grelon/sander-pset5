package com.example.sander.sander_pset5;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;
import java.util.List;

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

        // get databasehelper
        db.getInstance(getApplicationContext());

        // get views
        lvLists = (ListView) findViewById(R.id.lvMainLists);

        // update list with todolists
        updateList();

        // testing
        Log.d("log", "Database:");
        Log.d("log", db.read().toString());
    }

    private void updateList() {
        // store all lists from db in lists
        lists = db.read();

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
