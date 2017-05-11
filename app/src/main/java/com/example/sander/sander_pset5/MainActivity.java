package com.example.sander.sander_pset5;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sander.sander_pset5.todo.TodoList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvLists;
    ArrayAdapter<TodoList> todoListArrayAdapter;
    List<TodoList> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvLists = (ListView) findViewById(R.id.lvMainLists);
        lvLists.setAdapter(todoListArrayAdapter);


    }
}
