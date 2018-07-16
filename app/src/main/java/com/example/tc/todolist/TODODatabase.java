package com.example.tc.todolist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {TodoItem.class},version = 1)
 abstract class TODODatabase extends RoomDatabase {

    abstract TodoDAO gettodoDAO();

}
