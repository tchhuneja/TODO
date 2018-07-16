package com.example.tc.todolist;

import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    List<TodoItem> todos=new ArrayList<>();
    TodoAdapter adapter;
    TodoDAO todoDAO;

    String newtitle,newdescription;

    public static final int EDIT_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d("MainActivity","onCreate called");

        ListView listView=findViewById(R.id.list);

        TODODatabase database= Room.databaseBuilder(getApplicationContext(),TODODatabase.class,"Todo_db").allowMainThreadQueries().build();
        todoDAO=database.gettodoDAO();
        todos=todoDAO.getTodos();

//        TODOOpenHelper openHelper=new TODOOpenHelper(this);
//        SQLiteDatabase database=openHelper.getReadableDatabase();
//        Cursor cursor=database.query(Contract.TODOS.TODO_TABLE_NAME,null,null,null,null,null,null);
//        while (cursor.moveToNext()){
//            String title=cursor.getString(cursor.getColumnIndex(Contract.TODOS.COLUMN_TITLE));
//            String description=cursor.getString(cursor.getColumnIndex(Contract.TODOS.COLUMN_DESCRIPTION));
//
//            TodoItem item=new TodoItem(title,description);
//            todos.add(item);
//        }
//        cursor.close();

        adapter=new TodoAdapter(this,todos);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setCancelable(false);
        builder.setMessage("You really wish to delete this item?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TodoItem item=todos.get(position);
                todos.remove(position);
                adapter.notifyDataSetChanged();
                todoDAO.removeTodo(item);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        adddialog();
        return super.onOptionsItemSelected(item);
    }

    public void adddialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("TODO Item");
        builder.setCancelable(false);

        LinearLayout linearLayout =new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
        linearLayout.setLayoutParams(layoutParams);

        final EditText titlebox=new EditText(this);
        titlebox.setHint("Title");
        linearLayout.addView(titlebox);

        final EditText descriptionbox=new EditText(this);
        descriptionbox.setHint("Description");
        linearLayout.addView(descriptionbox);

        builder.setView(linearLayout);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newtitle=titlebox.getText().toString();
                newdescription=descriptionbox.getText().toString();
                TodoItem todoItem=new TodoItem(newtitle,newdescription);

                todoDAO.addTodo(todoItem);
//                TODOOpenHelper todoOpenHelper=new TODOOpenHelper(MainActivity.this);
//                SQLiteDatabase database=todoOpenHelper.getWritableDatabase();
//
//                ContentValues contentValues=new ContentValues();
//                contentValues.put(Contract.TODOS.COLUMN_TITLE,newtitle);
//                contentValues.put(Contract.TODOS.COLUMN_DESCRIPTION,newdescription);
//
//                long id=database.insert(Contract.TODOS.TODO_TABLE_NAME,null,contentValues);

                todos.add(todoItem);
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("TODO");
        builder.setCancelable(false);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle=new Bundle();
                TodoItem item=todos.get(position);
                bundle.putString("Title",item.getTitle());
                bundle.putString("Description",item.getDescription());
                bundle.putInt("Position",position);

                Intent intent =new Intent(MainActivity.this,EditTodo.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,EDIT_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        LinearLayout linearLayout=new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(layoutParams);

        TodoItem item=todos.get(position);

        final TextView TITLE = new TextView(this);
        TITLE.setText(item.getTitle());
        TITLE.setTypeface(null, Typeface.BOLD);
        linearLayout.addView(TITLE);

        final TextView DESCRIPTION = new TextView(this);
        DESCRIPTION.setText(item.getDescription());
        linearLayout.addView(DESCRIPTION);

        builder.setView(linearLayout);

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==EDIT_REQUEST_CODE){
            if (resultCode==EditTodo.RESULT_CODE){

                String title=data.getStringExtra(EditTodo.TITLE_KEY);
                String description=data.getStringExtra(EditTodo.DESCRIPTION_KEY);
                int position=data.getIntExtra(EditTodo.POSITION_KEY,2);

                TodoItem item=todos.get(position);
                item.setDescription(description);
                item.setTitle(title);

                todoDAO.updateTodo(item);

                adapter.notifyDataSetChanged();
            }
        }
    }
//
//    @Override
//    protected void onPause() {
//        Log.d("MainActivity","onPause called");
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        Log.d("MainActivity","onResume called");
//        super.onResume();
//    }
//
//    @Override
//    protected void onStop() {
//        Log.d("MainActivity","onStop called");
//        super.onStop();
//    }
//
//    @Override
//    protected void onStart() {
//        Log.d("MainActivity","onStart called");
//        super.onStart();
//    }
//
//    @Override
//    protected void onDestroy() {
//        Log.d("MainActivity","onCDestroy called");
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onRestart() {
//        Log.d("MainActivity","onRestart called");
//        super.onRestart();
//    }


}