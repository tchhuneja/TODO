package com.example.tc.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    List<TodoItem> todos=new ArrayList<>();
    TodoAdapter adapter;
    TodoDAO todoDAO;
    String newtitle,newdescription;

    public static final int EDIT_REQUEST_CODE=1;

    int month,day,year,hour,min;

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

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshList();
            }
        };

        IntentFilter intentFilter =  new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        registerReceiver(receiver,intentFilter);


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
        if (item.getItemId()==R.id.add)
            adddialog();

        else
            startActivity(new Intent(this,Settings.class));

        return super.onOptionsItemSelected(item);
    }

    public void adddialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("TODO Item");
        builder.setCancelable(false);

        LinearLayout linearLayout =new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0);
        linearLayout.setLayoutParams(layoutParams);

        final EditText titlebox=new EditText(this);
        titlebox.setHint("Title");
        linearLayout.addView(titlebox);

        final EditText descriptionbox=new EditText(this);
        descriptionbox.setHint("Description");
        linearLayout.addView(descriptionbox);

        final EditText datebox=new EditText(this);
        datebox.setHint("Select Date");
        linearLayout.addView(datebox);

        final EditText timebox=new EditText(this);
        timebox.setHint("Set time");
        linearLayout.addView(timebox);

        builder.setView(linearLayout);

        datebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int tempMonth = month+1;
                        // dateExpense = dayOfMonth + "/" + tempMonth + "/" + year;
                        datebox.setText(dayOfMonth + "/" + tempMonth + "/" + year);

                        MainActivity.this.day = dayOfMonth;
                        MainActivity.this.month  = month;
                        MainActivity.this.year  = year;

                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        timebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        timebox.setText(hourOfDay + ":" + minute);

                        MainActivity.this.hour = hourOfDay;
                        MainActivity.this.min = minute;

                    }
                },hour,min,false);

                timePickerDialog.show();
            }
        });

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newtitle=titlebox.getText().toString();
                newdescription=descriptionbox.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour,min);

                TodoItem todoItem=new TodoItem(newtitle,newdescription);
                todoItem.setTimeInEpochs(calendar.getTimeInMillis());

                todoDAO.addTodo(todoItem);

                int id=todoDAO.gethighestid();

                TodoItem item=todoDAO.gettodoitem(id);
                todos.add(item);
                adapter.notifyDataSetChanged();
//                TODOOpenHelper todoOpenHelper=new TODOOpenHelper(MainActivity.this);
//                SQLiteDatabase database=todoOpenHelper.getWritableDatabase();
//
//                ContentValues contentValues=new ContentValues();
//                contentValues.put(Contract.TODOS.COLUMN_TITLE,newtitle);
//                contentValues.put(Contract.TODOS.COLUMN_DESCRIPTION,newdescription);
//
//                long id=database.insert(Contract.TODOS.TODO_TABLE_NAME,null,contentValues);
            }
        });

        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
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

        final TextView date=new TextView(this);
        date.setText(item.getDate());
        linearLayout.addView(date);

        final TextView time=new TextView(this);
        time.setText(item.getTime());
        linearLayout.addView(time);

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

    public void refreshList(){
        int id=todoDAO.gethighestid();
        TodoItem todoItem= todoDAO.gettodoitem(id);
        todos.add(todoItem);

//        todos.clear();
//        todos.addAll(todoDAO.getTodos());

        adapter.notifyDataSetChanged();
    }

}