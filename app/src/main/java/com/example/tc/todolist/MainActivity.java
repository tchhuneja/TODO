package com.example.tc.todolist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener{

    ArrayList<TodoItem> todos=new ArrayList<>();
    TodoAdapter adapter;

    String newtitle,newdescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=findViewById(R.id.list);

        adapter=new TodoAdapter(this,todos);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
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
                todos.remove(position);
                adapter.notifyDataSetChanged();
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
                todos.add(todoItem);
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();

    }
}
