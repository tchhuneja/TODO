package com.example.tc.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter{
    private ArrayList<TodoItem> items;
    private LayoutInflater inflater;

     TodoAdapter(@NonNull Context context, @NonNull ArrayList<TodoItem> objects) {
        super(context,0, objects);
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items=objects;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View output=convertView;
         if (output==null)
             output=inflater.inflate(R.layout.todo_row,parent,false);
         TextView TITLE=output.findViewById(R.id.titletv);
         TextView DESCRIPTION=output.findViewById(R.id.descriptiontv);
         TodoItem todoItem=items.get(position);
         TITLE.setText(todoItem.getTitle());
         DESCRIPTION.setText(todoItem.getDescription());
         return output;
    }
}
