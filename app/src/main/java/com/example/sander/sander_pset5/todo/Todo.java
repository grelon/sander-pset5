package com.example.sander.sander_pset5.todo;

/**
 * Created by sander on 11-5-17.
 *
 * Defines a todo
 */

public class Todo {
    private int id;
    private int list_id;
    private String title;
    private String description;
    private int checked;

    public Todo(int list_id, String title, String description, int checked) {
        this.list_id = list_id;
        this.title = title;
        this.description = description;
        this.checked = checked;
    }

    public Todo() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", checked=" + checked +
                '}';
    }
}
