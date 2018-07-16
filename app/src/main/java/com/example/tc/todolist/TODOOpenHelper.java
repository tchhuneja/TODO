package com.example.tc.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TODOOpenHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="todo_db";
    private static final int VERSION=1;

    public TODOOpenHelper(Context context) {super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String todosql="CREATE TABLE " +
                        Contract.TODOS.TODO_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        Contract.TODOS.COLUMN_TITLE +
                        " TEXT," +
                        Contract.TODOS.COLUMN_DESCRIPTION +
                        " TEXT)";
        db.execSQL(todosql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
