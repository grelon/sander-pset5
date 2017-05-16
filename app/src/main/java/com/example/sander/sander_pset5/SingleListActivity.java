package com.example.sander.sander_pset5;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sander.sander_pset5.todo.Todo;
import com.example.sander.sander_pset5.todo.TodoList;

import java.util.ArrayList;

public class SingleListActivity extends AppCompatActivity {

    TodoList intentList;
    TodoList todolist;
    ArrayList<String> strList;
    ArrayAdapter arrayAdapter;

    ListView lvSingleList;
    EditText etSingleList;
    Button buttonSingleList;
    TextView tvSingleList;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Log.d("log", "Single.onCreate: start");

        // set actionbar title
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.singleListTitle);

        // get dbhelper
        db = db.getInstance(getApplicationContext());

        // get views
        lvSingleList = (ListView) findViewById(R.id.singleListView);
        etSingleList = (EditText) findViewById(R.id.singleEditText);
        buttonSingleList = (Button) findViewById(R.id.singleButton);
        tvSingleList = (TextView) findViewById(R.id.singleTitle);

        // set up list
        updateList();

        tvSingleList.setText(todolist.getTitle());


        Log.d("log", "Single.onCreate: success");
    }

    private void updateList() {
        // init the todolist
        initList();

        // init listview
        initListView();

        // set listeners
        setListListeners();
    }

    private void setListListeners() {
        lvSingleList.setOnItemClickListener(new simpleListener());
        lvSingleList.setOnItemLongClickListener(new longListener());
    }

    private void initListView() {
        Log.d("log", "Single.onCreate.initListview: start");

        // set choice mode to multiple
        lvSingleList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // initialize and set array adapter
        arrayAdapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, strList);
        lvSingleList.setAdapter(arrayAdapter);

        // set check state of items
        for (int i = 0; i < strList.size(); i++) {
            lvSingleList.setItemChecked(i, (todolist.getList().get(i).getChecked() != 0));
            Log.d("log", "setItemChecked success");
        }

        Log.d("log", "Single.onCreate.initListview: success");
    }

    private void initList() {
        Log.d("log", "Single.onCreate.initList: start");

        // get list from intent
        intentList = (TodoList) getIntent().getSerializableExtra("list");

        // store selected todolist in todolist
        for (TodoList list: db.readLists()) {
            if (list.getId() == intentList.getId()) {
                todolist = list;
            }
        }

        // fill strList with the text from the todos
        strList = new ArrayList<>();
        for (Todo todo: todolist.getList()) {
            strList.add(todo.getText().toString());
        }

        Log.d("log", "Single.onCreate.initList: success");
    }

    public void createTodo(View view) {
        /* creates a new todo when add is clicked */
        // construct new Todo object
        Todo todo = new Todo();
        todo.setList_id(todolist.getId());
        todo.setText(etSingleList.getText().toString());

        // create in db
        db.getInstance(getApplicationContext());
        db.createTodo(todo);

        // wrap up
        etSingleList.getText().clear();
        updateList();
    }

    private class simpleListener implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // reverse checkstate of item in database
            reverseCheckState(todolist.getList().get(position));

            // update db
            db.getInstance(getApplicationContext());
            db.updateTodo(todolist.getList().get(position));
        }
    }

    private void reverseCheckState(Todo todo) {
        // reverse check state in todo
        if (todo.getChecked() == 0) {todo.setChecked(1);}
        else {todo.setChecked(0);}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private class longListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // delete todo from db
            db.getInstance(getApplicationContext());
            db.deleteTodo(todolist.getList().get(position));

            // edit list accordingly
            updateList();

            return true;
        }
    }
}
