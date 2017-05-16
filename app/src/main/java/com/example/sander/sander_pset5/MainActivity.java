package com.example.sander.sander_pset5;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvLists;
    EditText etMain;
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
        etMain = (EditText) findViewById(R.id.mainEditText);

        // update list with todolists
        updateList();
        Log.d("log", "Main.onCreate: updateList() success");

    }

    // TODO: 12-5-17 Still need to test this properly
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close(); // unreachable
        Log.d("log", "Main.onDestroy: db.close() success");
    }

    private void updateList() {
        Log.d("log", "Main.onCreate.updateList: start");

        // store all lists from db in lists
        lists = db.readLists();
        Log.d("log", "Main.onCreate.updateList: db.read() succes");

        // store titles of lists
        listTitles = new ArrayList<>();
        for (TodoList list : lists) {
            listTitles.add(list.getTitle());
        }

        // create adapter for lvLists
        todoListArrayAdapter = new ArrayAdapter<> (getApplicationContext(),
                android.R.layout.simple_list_item_1, listTitles);

        // set adapter to lvLists
        lvLists.setAdapter(todoListArrayAdapter);

        setListListeners();
    }

    private void setListListeners() {
        lvLists.setOnItemClickListener(new simpleListListener());
        lvLists.setOnItemLongClickListener( new longListListener());
    }

    public void createList(View view) {
        Log.d("log", "createList clicked");

        // creates list when clicked
        TodoList list = new TodoList();

        Log.d("log", "et text:" + etMain.getText().toString());
        list.setTitle(etMain.getText().toString());
        db.createList(list);


        // wrap up
        etMain.getText().clear();
        updateList();
    }

    private class simpleListListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // start SingleListActivity

            Intent singleListIntent = new Intent(getApplicationContext(), SingleListActivity.class);
            singleListIntent.putExtra("list", lists.get(position));
            startActivity(singleListIntent);
        }
    }

    private class longListListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // delete list from db
            db.getInstance(getApplicationContext());
            db.deleteList(lists.get(position));

            // delete todos from db
            for (Todo todo: lists.get(position).getList()) {db.deleteTodo(todo);}

            // update listview
            updateList();

            return true;
        }
    }
}
