package com.example.tc.todolist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TodoDAO {

    @Insert
    void addTodo(TodoItem todoItem);

    @Delete
    void removeTodo(TodoItem todoItem);

    @Update
    void updateTodo(TodoItem todoItem);

    @Query("select * from TodoItem")
    List<TodoItem> getTodos();
}
