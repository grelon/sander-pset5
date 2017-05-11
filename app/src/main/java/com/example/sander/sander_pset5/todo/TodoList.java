package com.example.sander.sander_pset5.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sander on 11-5-17.
 *
 * Defines todolist class
 */

public class TodoList<Todo> extends ArrayList {
    private String id;
    private String title;
    private List<Todo> list;

    public TodoList(String title, List<Todo> list) {
        this.title = title;
        this.list = list;
    }

    public TodoList(String title, List<Todo> list, String id) {
        this.title = title;
        this.list = list;
        this.id = id;
    }

    public TodoList() {
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Todo> getList() {
        return list;
    }

    public void setList(List<Todo> list) {
        this.list = list;
    }
}
