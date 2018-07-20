package com.example.tc.todolist;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;


//we created this class to add todos via sms in background also
//if we don't need to add the sms received in background,we can create a local receiver in the onCreate() of main_activity
public class SMSReceiver extends BroadcastReceiver {

    String todo_title,todo_desc;
    String date,time;
    TodoDAO todoDAO;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        TODODatabase database = Room.databaseBuilder( context.getApplicationContext(),TODODatabase.class,"Todo_db").allowMainThreadQueries().build();
        todoDAO=database.gettodoDAO();

        Bundle data=intent.getExtras();

        if (data != null){
            Object[] pdus= (Object[]) data.get("pdus");

            for (int i=0;i<pdus.length;i++){

                SmsMessage smsMessage=SmsMessage.createFromPdu((byte[])pdus[i]);

                todo_desc=smsMessage.getDisplayMessageBody();
                todo_title=smsMessage.getDisplayOriginatingAddress();

//                long timestampMillis=smsMessage.getTimestampMillis()*1000L;
            }

            TodoItem todoItem=new TodoItem(todo_title,todo_desc);
//            Calendar calendar=Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            ++month;
//
//            date = day + "/" + month + "/" + year;
//
//            int hour = calendar.get(Calendar.HOUR);
//            int min = calendar.get(Calendar.MINUTE);
//
//            time = toString().valueOf(hour) + ":" + toString().valueOf(min);

            todoItem.setTimeInEpochs(System.currentTimeMillis());
            //todoItem.setDate(date);
           // todoItem.setTime(time);

            todoDAO.addTodo(todoItem);


            Toast.makeText(context, "Todo added", Toast.LENGTH_LONG).show();
        }
    }
}
