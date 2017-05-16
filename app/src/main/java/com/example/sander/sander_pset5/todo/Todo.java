package com.example.sander.sander_pset5.todo;

import java.io.Serializable;

/**
 * Created by sander on 11-5-17.
 *
 * Defines a todo
 */

public class Todo implements Serializable{
    private int id;
    private int list_id;
    private String text;
    private int checked;

    public Todo(int list_id, String text, int checked) {
        this.list_id = list_id;
        this.text = text;
        this.checked = checked;
    }

    public Todo() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getList_id() {
        return list_id;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", list_id=" + list_id +
                ", text='" + text + '\'' +
                ", checked=" + checked +
                '}';
    }
}
